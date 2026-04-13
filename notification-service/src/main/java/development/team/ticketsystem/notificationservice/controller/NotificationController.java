package development.team.ticketsystem.notificationservice.controller;

import development.team.ticketsystem.notificationservice.dto.ErrorResponse;
import development.team.ticketsystem.notificationservice.dto.NotificationCreationDto;
import development.team.ticketsystem.notificationservice.dto.NotificationDto;
import development.team.ticketsystem.notificationservice.exceptions.NotificationFormatException;
import development.team.ticketsystem.notificationservice.service.NotificationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RequestMapping("/notifications")
@RestController
@RequiredArgsConstructor
@Tag(name = "Контроллер нотификаций", description = "Управление уведомлениями")
@SecurityRequirement(name = "bearerAuth")
public class NotificationController {

    private final NotificationService notificationService;

    /**
     * Получение всех уведомлений всех пользователей
     *
     * @return список уведомлений
     */
    @GetMapping
    @Operation(summary = "Получить уведомления",
            description = "Возвращает уведомления всех пользователей. Доступно только администраторам")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Успешное получение списка уведомлений",
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            array = @ArraySchema(schema = @Schema(implementation = NotificationDto.class))
                    )
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "Внутренняя ошибка сервера",
                    content = @Content(
                            mediaType = MediaType.APPLICATION_PROBLEM_JSON_VALUE,
                            schema = @Schema(implementation = ErrorResponse.class)
                    )
            )
    })
    public ResponseEntity<List<NotificationDto>> getAllNotifications() {
        List<NotificationDto> result = this.notificationService.getAllNotifications();

        return ResponseEntity.ok().body(result);
    }

    /**
     * Добавление нового уведомления через DTO
     *
     * @param dto DTO для добавления нового уведомления
     */
    @PostMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(
            summary = "Создать уведомление",
            description = "Создает новое уведомление для пользователя на основе переданного DTO"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "201",
                    description = "Уведомление успешно создано",
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = NotificationDto.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Ошибка валидации входных данных",
                    content = @Content(
                            mediaType = MediaType.APPLICATION_PROBLEM_JSON_VALUE,
                            schema = @Schema(implementation = ErrorResponse.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "Внутренняя ошибка сервера",
                    content = @Content(
                            mediaType = MediaType.APPLICATION_PROBLEM_JSON_VALUE,
                            schema = @Schema(implementation = ErrorResponse.class)
                    )
            )
    })
    public ResponseEntity<NotificationDto> addNewNotification(
            @Valid @RequestBody NotificationCreationDto dto
    ) throws NotificationFormatException {
        NotificationDto notification = notificationService.addNewNotification(dto);
        return ResponseEntity.status(201).body(notification);
    }

    /**
     * Получение всех уведомлений конкретного пользователя
     *
     * @param userId ID конкретного пользователя
     */
    @GetMapping("/{userId}")
    @Operation(summary = "Получить уведомления пользователя",
            description = "Получение всех уведомления конкретного пользователя")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Успешное получение уведомлений пользователя",
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            array = @ArraySchema(schema = @Schema(implementation = NotificationDto.class))
                    )
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Некорректный формат ID пользователя",
                    content = @Content(
                            mediaType = MediaType.APPLICATION_PROBLEM_JSON_VALUE,
                            schema = @Schema(implementation = ErrorResponse.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "Внутренняя ошибка сервера",
                    content = @Content(
                            mediaType = MediaType.APPLICATION_PROBLEM_JSON_VALUE,
                            schema = @Schema(implementation = ErrorResponse.class)
                    )
            )
    })
    public ResponseEntity<List<NotificationDto>> getNotificationsOfSpecificUser(
            @Parameter(
            description = "Уникальный идентификатор пользователя в формате UUID",
            required = true,
            example = "550e8400-e29b-41d4-a716-446655440001",
            schema = @Schema(
                    type = "string",
                    format = "uuid",
                    description = "UUID пользователя"
            )
    ) @PathVariable UUID userId) {
        List<NotificationDto> result = this.notificationService.getAllNotifications(userId);

        return ResponseEntity.ok().body(result);
    }

    @DeleteMapping("/{userId}")
    public ResponseEntity<?> deleteAllUserNotifications(@PathVariable UUID userId) {
        this.notificationService.deleteAllUserNotifications(userId);

        return ResponseEntity.ok().build();
    }

    @Operation(summary = "Удалить уведомление пользователя по ID")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Успешное удаление уведомлений пользователя",
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            array = @ArraySchema(schema = @Schema(implementation = NotificationDto.class))
                    )
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Некорректный формат ID пользователя",
                    content = @Content(
                            mediaType = MediaType.APPLICATION_PROBLEM_JSON_VALUE,
                            schema = @Schema(implementation = ErrorResponse.class)
                    )
            )
    })
    @DeleteMapping("{userId}/{notificationId}")
    public ResponseEntity<?> deleteNotificationById(@PathVariable UUID userId, @PathVariable UUID notificationId) {
        this.notificationService.deleteUserNotificationById(userId, notificationId);

        return ResponseEntity.ok().build();
    }

    @PutMapping("/{notificationId}")
    public ResponseEntity<?> setReadStatusForNotification(@PathVariable UUID notificationId) {

    }
}
