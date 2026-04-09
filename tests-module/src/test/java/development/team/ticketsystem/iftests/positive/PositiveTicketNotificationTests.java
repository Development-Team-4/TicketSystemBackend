package development.team.ticketsystem.iftests.positive;

import com.fasterxml.jackson.databind.ObjectMapper;
import development.team.ticketsystem.iftests.configuration.RestConfiguration;
import development.team.ticketsystem.iftests.configuration.TestRestScenarioPositive;
import development.team.ticketsystem.iftests.configuration.UnifiedTestConfiguration;
import development.team.ticketsystem.iftests.dto.AuthResponse;
import development.team.ticketsystem.iftests.dto.CreateTicketRequest;
import development.team.ticketsystem.iftests.dto.NotificationDto;
import development.team.ticketsystem.iftests.dto.RegisterRequest;
import development.team.ticketsystem.iftests.mapper.TestJsonMapper;
import jakarta.annotation.PostConstruct;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestClient;

import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(classes = UnifiedTestConfiguration.class)
@DisplayName("Позитивные интеграционные тесты")
public class PositiveTicketNotificationTests {
    @Autowired
    private RestConfiguration restConfiguration;

    private final RestClient restClient = RestClient.builder().build();

    @Autowired
    private ObjectMapper objectMapper;

    private static final String TEST_TICKET_ID = "550e8400-e29b-41d4-a716-446655440000";
    private static final UUID TEST_CATEGORY_ID = UUID.randomUUID();

    private String accessToken;

    @PostConstruct
    private void postConstruct() {
        String url = this.restConfiguration.getUrls().getBaseUrl() + ":"
                + this.restConfiguration.getUrls().getTicket().getPort()
                + this.restConfiguration.getUrls().getTicket().getCommonUrl();

        // Создание тикета в системе
        this.restClient.post()
                .uri(url)
                .body(new CreateTicketRequest(
                "", "", TEST_CATEGORY_ID
                ));

        // Регистрация пользователя
        String urlRegister = this.restConfiguration.getUrls().getBaseUrl() + ":"
                + this.restConfiguration.getUrls().getGateway().getPort()
                + this.restConfiguration.getUrls().getAuth().getAuthUrl()
                + this.restConfiguration.getUrls().getAuth().getRegister();

        AuthResponse response = this.restClient.post()
                .uri(urlRegister)
                .body(new RegisterRequest(
                        "ааа",
                        "fff@examle.ru",
                        "fff"
                ))
                .retrieve()
                .body(AuthResponse.class);

        this.accessToken = response.getAccessToken();
    }

    @Test
    @DisplayName("ИФТ Тест 1 : Проверка связности notificationservice и ticketservice (комментарии)")
    void notificationTicketTestComments() throws Exception {
        commonTestNotificationTicket(
                TestRestScenarioPositive.TEST_CREATE_COMMENT,
                TEST_TICKET_ID
        );
    }

    @Test
    @DisplayName("ИФТ Тест 2 : Проверка связности notificationservice и ticketservice (изменение статуса)")
    void notificationTicketTestChangeStatus() throws Exception {
        commonTestNotificationTicket(
                TestRestScenarioPositive.TEST_CHANGE_STATUS,
                TEST_TICKET_ID
        );
    }

    @Test
    @DisplayName("ИФТ Тест 3 : Проверка связности notificationservice и ticketservice (назначение)")
    void notificationTicketTestAssignee() throws Exception {
        commonTestNotificationTicket(
                TestRestScenarioPositive.TEST_ASSIGNEE,
                TEST_TICKET_ID
        );
    }

    @Test
    @DisplayName("ИФТ Тест 4 : Проверка связности notificationservice и ticketservice (создание тикета)")
    void notificationTicketTestCreateTicket() throws Exception {
        commonTestNotificationTicket(
                TestRestScenarioPositive.TEST_CREATE_TICKET,
                null
        );
    }

    @Test
    @DisplayName("ИФТ Тест 5 : Проверка связности notificationservice и ticketservice (обновление тикета)")
    void notificationTicketTestUpdateTicket() throws Exception {
        commonTestNotificationTicket(
                TestRestScenarioPositive.TEST_UPDATE_TICKET,
                TEST_TICKET_ID
        );
    }

    @Test
    @DisplayName("ИФТ Тест 6 : Получение всех тикетов через админа")
    void gettingAllTicketsViaAdminRole() {

    }

    /**
     * Общий тест для notification-ticket
     * @param scenario сценарий
     * @param ticketId id тикета
     * @throws Exception
     */
    private void commonTestNotificationTicket(
            TestRestScenarioPositive scenario,
            String ticketId) throws Exception {

        // 1. Формирование сообщения из JSON файла
        String message = TestJsonMapper.readDataFromJson(scenario.getJsonMessageType());
        System.out.println(message);

        // 2. Формирование URL для отправки запроса
        String urlSending = buildUrl(scenario, ticketId);

        // 3. Отправка запроса в ticketService
        ResponseEntity<String> ticketResponse = sendRequest(urlSending, message, scenario.getHttpMethod());

        // Проверка успешности запроса
        assertNotNull(ticketResponse, "Ответ от ticketService не должен быть null");
        assertTrue(
                ticketResponse.getStatusCode().is2xxSuccessful(),
                String.format("Запрос к ticketService должен быть успешным. Статус: %s, URL: %s",
                        ticketResponse.getStatusCode(), urlSending)
        );

        // Ожидание для обработки асинхронных уведомлений
        Thread.sleep(1500);

        List<NotificationDto> notifications = getAllNotifications();

        boolean hasNewNotification = verifyNotificationExists(notifications, scenario);

        assertTrue(hasNewNotification,
                String.format("Должно быть создано новое уведомление для сценария: %s", scenario.name()));
    }

    /**
     * Билда url из шаблона в enum
     * @param scenario сценарий
     * @param ticketId id тикета
     * @return сформированный URL
     */
    private String buildUrl(TestRestScenarioPositive scenario, String ticketId) {
        String urlTemplate = scenario.getUrlFieldName();

        if (scenario.isRequiresId() && ticketId != null) {
            return String.format(urlTemplate, ticketId);
        }

        return urlTemplate;
    }

    /**
     * Метод отправки запроса
     * @param url url запроса
     * @param body тело запроса
     * @param method HTTP-метод
     * @return сущность ответа
     */
    private ResponseEntity<String> sendRequest(String url, String body, HttpMethod method) {
        return restClient.method(method)
                .uri(url)
                .body(body)
                .header("Authorization", "Bearer " + this.accessToken)
                .retrieve()
                .toEntity(String.class);
    }

    /**
     * Получение всех нотификаций
     * @return список dto нотификаций
     */
    private List<NotificationDto> getAllNotifications() {
        String url = this.restConfiguration.getUrls().getBaseUrl() + ":"
                + this.restConfiguration.getUrls().getNotification().getPort()
                + this.restConfiguration.getUrls().getNotification().getCommonUrl();

        try {
            ResponseEntity<List<NotificationDto>> response = restClient.get()
                    .uri(url)
                    .header("Authorization", "Bearer " + this.accessToken)
                    .retrieve()
                    .toEntity(new org.springframework.core.ParameterizedTypeReference<List<NotificationDto>>() {});

            if (response.getStatusCode().is2xxSuccessful()) {
                return response.getBody();
            } else {
                return List.of();
            }
        } catch (Exception e) {
            return List.of();
        }
    }

    /**
     * Проверка наличия уведомления
     *
     * @param notifications список уведомлений
     * @param scenario сценарий
     * @return true, если уведомление есть, false иначе
     */
    private boolean verifyNotificationExists(List<NotificationDto> notifications, TestRestScenarioPositive scenario) {
        if (notifications == null || notifications.isEmpty()) {
            return false;
        }

        long currentTime = System.currentTimeMillis();

        return notifications.stream().anyMatch(notification -> {
            if (notification.getCreatedAt() == null) {
                return false;
            }

            long notificationTime = notification.getCreatedAt().getTime();
            boolean isRecent = (currentTime - notificationTime) < 5000;

            return isRecent;
        });
    }

}