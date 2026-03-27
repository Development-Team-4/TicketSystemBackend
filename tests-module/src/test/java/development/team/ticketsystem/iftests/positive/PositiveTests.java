package development.team.ticketsystem.iftests.positive;

import development.team.ticketsystem.iftests.configuration.RestConfiguration;
import development.team.ticketsystem.iftests.configuration.TestScenarioPositive;
import development.team.ticketsystem.iftests.configuration.TestScenarioSteps;
import development.team.ticketsystem.iftests.mapper.TestJsonMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.client.RestClient;

import java.util.List;

@ExtendWith(MockitoExtension.class)
@DisplayName("Позитивные интеграционные тесты")
@Slf4j
@RequiredArgsConstructor
public class PositiveTests {
    private final RestClient restClient;

    private final RestConfiguration restConfiguration;

    @BeforeEach
    public void beforeEach() {

    }

    @Test
    @DisplayName("ИФТ Тест 1 : Проверка связности notificationservice и ticketservice (комментарии)")
    void notificationTicketTestComments() throws Exception {
        commonTestNotificationTicket(null, TestScenarioSteps.builder()
                .sendPostRequest(true)
                .sendPutRequest(false)
                .build());
    }

    @Test
    @DisplayName("ИФТ Тест 2 : Проверка связности notificationservice и ticketservice (изменение статуса)")
    void notificationTicketTestChangeStatus() throws Exception {
        commonTestNotificationTicket(null, TestScenarioSteps.builder()
                .sendPostRequest(false)
                .sendPutRequest(true)
                .build());
    }

    @Test
    @DisplayName("ИФТ Тест 3 : Проверка связности notificationservice и ticketservice (изменение статуса)")
    void notificationTicketTestAssignee() throws Exception {
        commonTestNotificationTicket(null, TestScenarioSteps.builder()
                .sendPostRequest(false)
                .sendPutRequest(true)
                .build());
    }

    @Test
    @DisplayName("ИФТ Тест 4 : Проверка связности notificationservice и ticketservice (изменение статуса)")
    void notificationTicketTestCreateTicket() throws Exception {
        commonTestNotificationTicket(null, TestScenarioSteps.builder()
                .sendPostRequest(true)
                .sendPutRequest(false)
                .build());
    }

    @Test
    @DisplayName("ИФТ Тест 5 : Проверка связности notificationservice и ticketservice (изменение статуса)")
    void notificationTicketTestUpdateTicket() throws Exception {
        commonTestNotificationTicket(null, TestScenarioSteps.builder()
                .sendPostRequest(false)
                .sendPutRequest(true)
                .build());
    }

    private void commonTestNotificationTicket(TestScenarioPositive scenario, TestScenarioSteps steps) throws Exception {
        // Формирование сообщения
        String message = TestJsonMapper.readDataFromJson(scenario.getJsonMessageType());

        // Отправка сообщения (POST / PUT)
        String urlSending = RestConfiguration.BASIC_URL
                + restConfiguration.getTicketUrls().getPort()
                + restConfiguration.getTicketUrls().getCommonUrl()
                + scenario.getMethod();

        if(steps.hasAttributes()) {
            urlSending = String.format(urlSending, scenario.getAttribute());
        }

        if(steps.sendPostRequest()) {
            restClient.post()
                    .uri(urlSending)
                    .body(message);
        }

        if(steps.sendPutRequest()) {
            restClient.put()
                    .uri(urlSending)
                    .body(message);
        }

        // Получение уведомления
        String urlNotifications = RestConfiguration.BASIC_URL
                + restConfiguration.getNotificationUrls().getPort()
                + restConfiguration.getNotificationUrls().getCommonUrl()
                + restConfiguration.getNotificationUrls().getGetAllNotifications();

        List notificationDtos = restClient.get()
                .uri(urlNotifications)
                .retrieve()
                .body(List.class);
    }
}
