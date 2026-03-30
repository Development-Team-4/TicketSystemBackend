package development.team.ticketsystem.notificationservice.negative;

import development.team.ticketsystem.notificationservice.controller.NotificationController;
import development.team.ticketsystem.notificationservice.exceptions.handlers.GlobalExceptionHandler;
import development.team.ticketsystem.notificationservice.helper.JsonHelper;
import development.team.ticketsystem.notificationservice.repository.NotificationRepository;
import development.team.ticketsystem.notificationservice.service.NotificationService;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.http.MediaType;

import java.util.UUID;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

@ExtendWith(MockitoExtension.class)
@DisplayName("Негативные тесты для notification-service")
@Slf4j
public class NegativeTests {
    private MockMvc mockMvc;

    @Mock
    private NotificationRepository notificationRepository;

    @Mock
    private NotificationService notificationService;

    @InjectMocks
    private NotificationController mainController;

    @BeforeEach
    public void beforeEach() {
        this.mockMvc = MockMvcBuilders.standaloneSetup(mainController)
                .setControllerAdvice(new GlobalExceptionHandler()).build();
    }

    @Test
    @DisplayName("TC-4 : Некорректная отправка сообщения (неправильный type)")
    void incorrectTypeMessageSending() throws Exception {
        // Читаем JSON с некорректным type уведомления
        String invalidJson = JsonHelper.readDataFromJson("json/incorrect_type_message.json");

        log.info("Тест: Отправка с некорректным типом уведомления");
        log.info("JSON для отправки: {}", invalidJson);

        // Отправка сообщения + получаем ошибку
        this.mockMvc.perform(post("/notifications/" + UUID.randomUUID())
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(invalidJson))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("TC-5 : Некорректная формат DTO (отсутствует ticket_id)")
    void incorrectFieldTicketId() throws Exception {
        fullTestOfCorruptField("ticketId");
    }

    @Test
    @DisplayName("TC-6 : Некорректная формат DTO (отсутствует user_id)")
    void incorrectFieldUserId() throws Exception {
        fullTestOfCorruptField("userId");
    }

    @Test
    @DisplayName("TC-7 : Некорректная формат DTO (отсутствует type)")
    void incorrectFieldType() throws Exception {
        fullTestOfCorruptField("type");
    }

    /**
     * Полный тест с удалением поля из DTO
     *
     * @param field поле для удаления из DTO
     * @return true, если тест пройден, и false иначе
     * @throws Exception исключение в случае, если файл для DTO будет не найден
     */
    private boolean fullTestOfCorruptField(String field) throws Exception {
        // Читаем JSON с некорректным type уведомления
        String invalidJson = JsonHelper.readDataFromJson("json/correct_type_message.json");

        invalidJson = JsonHelper.deleteFieldFromJson(invalidJson, field);

        log.info("Тест: Отправка с удаленным полем из DTO");
        log.info("Поле для удаления: {}", field);
        log.info("JSON для отправки: {}", invalidJson);

        // Отправка сообщения + получаем ошибку
        this.mockMvc.perform(post("/notifications/" + UUID.randomUUID())
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(invalidJson))
                .andExpect(status().isBadRequest());
        return true;
    }
}
