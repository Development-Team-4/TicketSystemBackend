package development.team.ticketsystem.notification_service.controller;

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
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RequestMapping("/notifications")
@RestController
@RequiredArgsConstructor
@Tag(name = "Контроллер нотификаций", description = "Управление уведомлениями")
@Slf4j
public class MainController {

    private final NotificationService notificationService;

    /**
     * Получение всех уведомлений всех пользователей
     * (доступно только админам)
     */
    @GetMapping("/get")
    @Operation(summary = "Получить уведомления",
            description = "Возвращает уведомления всех пользователей")
    public ResponseEntity<List<Notification>> getAllNotifications() {
        log.info("Поступил запрос на получение всех уведомлений пользователей");

        List<Notification> result = this.notificationService.getAllNotifications();

        return ResponseEntity.ok().body(result);
    }

    /**
     * Добавление нового уведомления через DTO
     *
     * @param dto DTO для добавления нового уведомления
     */
    @PostMapping
    @Operation(summary = "Добавить уведомление",
            description = "Добавление уведомлений с помощью NotificationDTO")
    @ApiResponse(responseCode = "200", description = "Успешно добавлено")
    public ResponseEntity<?> addNewNotification(
            @Valid @Schema(description = "DTO уведомления", example = "{\"ticket_id\": " +
                    "\"e1238262-8f77-43a2-8df1-90266c2d25f2\", \n\"type\": \"COMMENT\"}")
            @RequestBody NotificationDto dto) {
        log.info("Поступил запрос на добавление нового уведомления с данными: {}", dto);

        boolean result = this.notificationService.addNewNotification(dto);

        if(result) {
            log.info("Уведомление успешно добавлено");

            return ResponseEntity.ok().build();
        } else {
            log.error("Возникла ошибка с поступившей информацией");

            return ResponseEntity.badRequest().build();
        }
    }

    /**
     * Получение всех уведомлений конкретного пользователя
     *
     * @param userId ID конкретного пользователя
     */
    @GetMapping("/{userId}")
    @Operation(summary = "Получить уведомления пользователя",
            description = "Получение всех уведомления конкретного пользователя")
    @ApiResponse(responseCode = "200", description = "Успешно найдено")
    public ResponseEntity<List<Notification>> getNotificationsOfSpecificUser(@Valid @Parameter(description = "ID пользователя")
                                                                 @PathVariable UUID userId) {
        log.info("Поступил запрос на выдачу всех уведомлений пользователя с user_id {}", userId);

        List<Notification> result = this.notificationService.getAllNotifications(userId);

        return ResponseEntity.ok().body(result);
    }
}
