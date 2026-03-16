package development.team.ticketsystem.notification_service.controller;

import development.team.ticketsystem.notification_service.dto.AddingNotificationDto;
import development.team.ticketsystem.notification_service.dto.NotificationDto;
import development.team.ticketsystem.notification_service.entity.Notification;
import development.team.ticketsystem.notification_service.service.NotificationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RequestMapping("/api")
@RestController
@RequiredArgsConstructor
@Tag(name = "Контроллер нотификаций", description = "Управление уведомлениями")
public class MainController {

    private final NotificationService notificationService;

    /**
     * Получение всех уведомлений всех пользователей
     */
    @GetMapping("/get")
    @Operation(summary = "Получить уведомления",
            description = "Возвращает уведомления всех пользователей")
    public List<Notification> getAllNotifications() {
        return null;
    }

    /**
     * Добавление нового уведомления через DTO
     *
     * @param dto DTO для добавления нового уведомления
     */
    @PostMapping("/add")
    @Operation(summary = "Добавить уведомление",
            description = "Добавление уведомлений с помощью NotificationDTO")
    @ApiResponse(responseCode = "200", description = "Успешно добавлено")
    public ResponseEntity<AddingNotificationDto> addNewNotification(
            @Valid @Schema(description = "DTO уведомления", example = "{\"user_id\": \"e1238262-8f77-43a2-8df1-90266c2d25f2\", \n\"ticket_id\": \"e1238262-8f77-43a2-8df1-90266c2d25f2\", \n\"type\": \"COMMENT\"}") NotificationDto dto) {
        return null;
    }

    /**
     * Получение всех уведомлений конкретного пользователя
     *
     * @param userId ID конкретного пользователя
     */
    @GetMapping("/{userId}/get")
    @Operation(summary = "Получить уведомления пользователя",
            description = "Получение всех уведомления конкретного пользователя")
    @ApiResponse(responseCode = "200", description = "Успешно найдено")
    public List<Notification> getNotificationsOfSpecificUser(@Valid @Parameter(description = "ID пользователя")
                                                                 @PathVariable UUID userId) {
        return null;
    }
}
