package development.team.ticketsystem.notificationservice.positive;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import development.team.ticketsystem.notificationservice.controller.NotificationController;
import development.team.ticketsystem.notificationservice.dto.NotificationCreationDto;
import development.team.ticketsystem.notificationservice.dto.NotificationDto;
import development.team.ticketsystem.notificationservice.entity.Notification;
import development.team.ticketsystem.notificationservice.entity.NotificationType;
import development.team.ticketsystem.notificationservice.exceptions.handlers.GlobalExceptionHandler;
import development.team.ticketsystem.notificationservice.mapper.NotificationMapper;
import development.team.ticketsystem.notificationservice.repository.NotificationRepository;
import development.team.ticketsystem.notificationservice.service.NotificationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
@DisplayName("Позитивные тесты для notification-service")
@Slf4j
@RequiredArgsConstructor
public class PositiveTests {
    private UUID userId;
    private UUID ticketId;

    private MockMvc mockMvc;

    private ObjectMapper objectMapper;

    @Mock
    private NotificationRepository notificationRepository;

    @Mock
    private NotificationService notificationService;

    @InjectMocks
    private NotificationController mainController;

    @BeforeEach
    public void beforeEach() {
        this.userId = UUID.randomUUID();
        this.ticketId = UUID.randomUUID();

        this.mockMvc = MockMvcBuilders.standaloneSetup(mainController)
                .setControllerAdvice(new GlobalExceptionHandler())
                .build();
        objectMapper = new ObjectMapper();
    }

    @Test
    @DisplayName("TC-1 : Корректная отправка нотификации по изменению статуса тикета")
    void correctSendingChangeStatus() throws Exception {
        assertTrue(fullTest(NotificationType.STATUS_CHANGE));
    }

    @Test
    @DisplayName("TC-2 : Корректная отправка нотификации по назначению тикета")
    void correctSendingAssignment() throws Exception {
        assertTrue(fullTest(NotificationType.ASSIGNMENT));
    }

    @Test
    @DisplayName("TC-3 : Корректная отправка нотификации по добавлению комментария")
    void correctSendingComment() throws Exception {
        assertTrue(fullTest(NotificationType.COMMENT));
    }

    /**
     * Метод полного теста для ТС-1-3
     *
     * @param type тип поступившего уведомления
     * @return true, если тест пройден, false иначе
     * @throws Exception для несуществующего эндпоинта/несуществующего формата для JSON
     */
    private boolean fullTest(NotificationType type) throws Exception {
        // Формирование сообщения
        NotificationCreationDto dto = new NotificationCreationDto(
                this.userId,
                this.ticketId,
                type
        );

        NotificationDto expectedNotification = new NotificationDto(
                this.userId,
                this.ticketId,
                type
        );

        when(notificationService.addNewNotification(any(NotificationCreationDto.class)))
                .thenReturn(expectedNotification);

        when(notificationService.getAllNotifications())
                .thenReturn(List.of(expectedNotification));

        log.info("Тест: Корректная отправка нотификации типа {}", type);
        log.info("1. Отправка сообщения {} на эндпоинт /notifications", dto);
        log.info("2. Получение сообщения в БД. Сравнение результатов");

        // Отправка сообщения по REST-у
        this.mockMvc.perform(post("/notifications/" + userId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().is(201));

        // Отправка запроса на получение всех нотификаций
        MvcResult notificationListMvcResult = this.mockMvc.perform(get("/notifications")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        List<NotificationDto> notificationList =
                this.objectMapper.readValue(
                        notificationListMvcResult.getResponse().getContentAsString(),
                        new TypeReference<List<NotificationDto>>() {}
                );

        // Проверка наличия добавленой нотификации в БД
        for(NotificationDto n : notificationList) {
            if(n.getSent() && n.getTicketId().equals(this.ticketId) && n.getUserId().equals(this.userId)) {
                return true;
            }
        }

        return false;
    }
}