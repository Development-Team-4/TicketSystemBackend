package development.team.ticketsystem.notification_service.negative;

import com.fasterxml.jackson.databind.ObjectMapper;
import development.team.ticketsystem.notification_service.controller.MainController;
import development.team.ticketsystem.notification_service.dto.NotificationDto;
import development.team.ticketsystem.notification_service.helper.JsonHelper;
import development.team.ticketsystem.notification_service.repository.NotificationRepository;
import development.team.ticketsystem.notification_service.service.NotificationService;
import jakarta.annotation.PostConstruct;
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

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

@ExtendWith(MockitoExtension.class)
@DisplayName("Негативные тесты для notification-service")
@Slf4j
public class NegativeTests {
    private UUID userId;
    private UUID ticketId;

    private MockMvc mockMvc;

    private ObjectMapper objectMapper;

    @Mock
    private NotificationRepository notificationRepository;

    @Mock
    private NotificationService notificationService;

    @InjectMocks
    private MainController mainController;

    @PostConstruct
    private void postConstruct() {
        doNothing().when(notificationService).addNewNotification(any(NotificationDto.class));
    }

    @BeforeEach
    public void beforeEach() {
        this.userId = UUID.randomUUID();
        this.ticketId = UUID.randomUUID();

        this.mockMvc = MockMvcBuilders.standaloneSetup(mainController).build();
        objectMapper = new ObjectMapper();
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
}
