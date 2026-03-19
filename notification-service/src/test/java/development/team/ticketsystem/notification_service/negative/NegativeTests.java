package development.team.ticketsystem.notification_service.negative;

import development.team.ticketsystem.notification_service.constants.DtoFields;
import development.team.ticketsystem.notification_service.controller.MainController;
import development.team.ticketsystem.notification_service.helper.JsonHelper;
import development.team.ticketsystem.notification_service.repository.NotificationRepository;
import development.team.ticketsystem.notification_service.service.NotificationService;
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
    private MainController mainController;

    @BeforeEach
    public void beforeEach() {
        this.mockMvc = MockMvcBuilders.standaloneSetup(mainController).build();
    }

    @Test
    @DisplayName("TC-4 : Некорректная отправка сообщения (неправильный type)")
    void incorrectTypeMessageSending() throws Exception {
        // Читаем JSON с некорректным type уведомления
        String invalidJson = JsonHelper.loadResourceAsString("json/incorrect_type_message.json");

        log.info("Тест: Отправка с некорректным типом уведомления");
        log.info("JSON для отправки: {}", invalidJson);

        // Отправка сообщения + получаем ошибку
        this.mockMvc.perform(post("/notifications")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(invalidJson))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("TC-5 : Некорректная формат DTO (отсутствует ticket_id)")
    void incorrectFieldTicketId() throws Exception {
        fullTestOfCorruptField(DtoFields.NotificationDto.TICKET_ID_FIELD);
    }

    @Test
    @DisplayName("TC-6 : Некорректная формат DTO (отсутствует user_id)")
    void incorrectFieldUserId() throws Exception {
        fullTestOfCorruptField(DtoFields.NotificationDto.USER_ID_FIELD);
    }

    @Test
    @DisplayName("TC-7 : Некорректная формат DTO (отсутствует type)")
    void incorrectFieldType() throws Exception {
        fullTestOfCorruptField(DtoFields.NotificationDto.TYPE_FIELD);
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
        String invalidJson = JsonHelper.loadResourceAsString("json/incorrect_type_message.json");

        invalidJson = JsonHelper.corruptField(invalidJson, field);

        log.info("Тест: Отправка с удаленным полем из DTO");
        log.info("Поле для удаления: {}", field);
        log.info("JSON для отправки: {}", invalidJson);

        // Отправка сообщения + получаем ошибку
        this.mockMvc.perform(post("/notifications")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(invalidJson))
                .andExpect(status().isBadRequest());
        return true;
    }
}
