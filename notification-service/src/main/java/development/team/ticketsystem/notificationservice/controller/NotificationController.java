package development.team.ticketsystem.notificationservice.controller;

import development.team.ticketsystem.notificationservice.dto.NotificationCreationDto;
import development.team.ticketsystem.notificationservice.dto.NotificationDto;
import development.team.ticketsystem.notificationservice.exceptions.NotificationFormatException;
import development.team.ticketsystem.notificationservice.service.NotificationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
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
public class NotificationController {

    private final NotificationService notificationService;

    /**
     * Получение всех уведомлений всех пользователей
     *
     * @return список уведомлений
     */
    @GetMapping("/get")
    @Operation(summary = "Получить уведомления",
            description = "Возвращает уведомления всех пользователей. Доступно только администраторам")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Успешное получение списка уведомлений"
            ),
            @ApiResponse(
                    responseCode = "403",
                    description = "Доступ запрещен - требуется роль администратора"
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "Внутренняя ошибка сервера"
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
    @PostMapping
    @Operation(summary = "Добавить уведомление",
            description = "Добавление уведомлений с помощью NotificationDTO")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "201",
                    description = "Уведомление успешно создано"
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Некорректные данные запроса"
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Ресурс не найден (тикет или пользователь не существует)"
            ),
            @ApiResponse(
                    responseCode = "422",
                    description = "Ошибка формата уведомления"
            )
    })
    public ResponseEntity<?> addNewNotification(
            @Valid
            @Parameter(
                    description = "DTO для создания уведомления",
                    required = true,
                    schema = @Schema(implementation = NotificationCreationDto.class),
                    examples = {
                            @ExampleObject(
                                    name = "Уведомление о комментарии",
                                    value = """
                                            {
                                                "ticket_id": "e1238262-8f77-43a2-8df1-90266c2d25f2",
                                                "type": "COMMENT"
                                            }
                                            """
                            ),
                            @ExampleObject(
                                    name = "Уведомление об изменении статуса",
                                    value = """
                                            {
                                                "ticket_id": "e1238262-8f77-43a2-8df1-90266c2d25f2",
                                                "type": "STATUS_CHANGE"
                                            }
                                            """
                            ),
                            @ExampleObject(
                                    name = "Уведомление о назначении",
                                    value = """
                                            {
                                                "ticket_id": "e1238262-8f77-43a2-8df1-90266c2d25f2",
                                                "type": "ASSIGNMENT"
                                            }
                                            """
                            )
                    }
            )
            @RequestBody NotificationCreationDto dto) throws NotificationFormatException {
        this.notificationService.addNewNotification(dto);

        return ResponseEntity.status(201).build();
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
                            schema = @Schema(implementation = NotificationDto.class),
                            examples = @ExampleObject(
                                    name = "Успешный ответ",
                                    value = """
                                            [
                                                {
                                                    "id": "550e8400-e29b-41d4-a716-446655440000",
                                                    "user_id": "550e8400-e29b-41d4-a716-446655440001",
                                                    "ticket_id": "550e8400-e29b-41d4-a716-446655440002",
                                                    "title": "Новый комментарий"
                                                    "type": "COMMENT",
                                                    "message": "К Вашему тикету добавлен новый комментарий",
                                                    "sent": true,
                                                    "created_at": "2024-01-15T10:30:00Z",
                                                    "updated_at": "2024-01-15T10:30:00Z"
                                                }
                                            ]
                                            """
                            )
                    )
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Некорректный формат ID пользователя"
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Пользователь не найден"
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "Внутренняя ошибка сервера"
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
}
