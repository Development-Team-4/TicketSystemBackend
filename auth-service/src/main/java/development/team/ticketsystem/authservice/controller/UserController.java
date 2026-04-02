package development.team.ticketsystem.authservice.controller;

import development.team.ticketsystem.authservice.dto.error.ErrorResponse;
import development.team.ticketsystem.authservice.dto.notification.NotificationSettingsResponse;
import development.team.ticketsystem.authservice.dto.notification.UpdateNotificationSettingsRequest;
import development.team.ticketsystem.authservice.dto.user.UpdateUserRequest;
import development.team.ticketsystem.authservice.dto.user.UpdateUserRoleRequest;
import development.team.ticketsystem.authservice.dto.user.UserResponse;
import development.team.ticketsystem.authservice.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@Slf4j
@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
@Tag(name = "Users", description = "Операции управления пользователями и их настройками уведомлений")
public class UserController {

    private final UserService userService;

    @Operation(
            summary = "Получить пользователя по ID",
            description = "Возвращает данные пользователя по его уникальному идентификатору"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Пользователь успешно найден",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = UserResponse.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Пользователь не найден",
                    content = @Content(
                            mediaType = "application/problem+json",
                            schema = @Schema(implementation = ErrorResponse.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "Внутренняя ошибка сервера",
                    content = @Content(
                            mediaType = "application/problem+json",
                            schema = @Schema(implementation = ErrorResponse.class)
                    )
            )
    })
    @GetMapping("/{id}")
    public UserResponse getUser(
            @Parameter(
                    description = "UUID пользователя",
                    required = true,
                    example = "550e8400-e29b-41d4-a716-446655440000"
            )
            @PathVariable UUID id
    ) {
        return userService.getById(id);
    }

    @Operation(
            summary = "Получить список пользователей",
            description = "Возвращает список всех пользователей системы"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Список пользователей успешно получен",
                    content = @Content(
                            mediaType = "application/json",
                            array = @ArraySchema(schema = @Schema(implementation = UserResponse.class))
                    )
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "Внутренняя ошибка сервера",
                    content = @Content(
                            mediaType = "application/problem+json",
                            schema = @Schema(implementation = ErrorResponse.class)
                    )
            )
    })
    @GetMapping
    public List<UserResponse> getUsers() {
        return userService.getAll();
    }

    @Operation(
            summary = "Обновить пользователя",
            description = "Обновляет имя и/или аватар пользователя по его идентификатору"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Пользователь успешно обновлен",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = UserResponse.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Пользователь не найден",
                    content = @Content(
                            mediaType = "application/problem+json",
                            schema = @Schema(implementation = ErrorResponse.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "Внутренняя ошибка сервера",
                    content = @Content(
                            mediaType = "application/problem+json",
                            schema = @Schema(implementation = ErrorResponse.class)
                    )
            )
    })
    @PutMapping("/{id}")
    public UserResponse updateUser(
            @Parameter(
                    description = "UUID пользователя",
                    required = true,
                    example = "550e8400-e29b-41d4-a716-446655440000"
            )
            @PathVariable UUID id,
            @RequestBody(
                    required = true,
                    description = "Новые данные пользователя",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = UpdateUserRequest.class)
                    )
            )
            @org.springframework.web.bind.annotation.RequestBody UpdateUserRequest request
    ) {
        return userService.update(id, request);
    }

    @Operation(
            summary = "Обновить настройки уведомлений пользователя",
            description = "Создает или обновляет настройки email и Telegram уведомлений для указанного пользователя"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Настройки уведомлений успешно обновлены",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = NotificationSettingsResponse.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Пользователь не найден",
                    content = @Content(
                            mediaType = "application/problem+json",
                            schema = @Schema(implementation = ErrorResponse.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "Внутренняя ошибка сервера",
                    content = @Content(
                            mediaType = "application/problem+json",
                            schema = @Schema(implementation = ErrorResponse.class)
                    )
            )
    })
    @PutMapping("/{id}/notification-settings")
    public NotificationSettingsResponse updateSettings(
            @Parameter(
                    description = "UUID пользователя",
                    required = true,
                    example = "550e8400-e29b-41d4-a716-446655440000"
            )
            @PathVariable UUID id,
            @RequestBody(
                    required = true,
                    description = "Новые настройки уведомлений пользователя",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = UpdateNotificationSettingsRequest.class)
                    )
            )
            @org.springframework.web.bind.annotation.RequestBody UpdateNotificationSettingsRequest request
    ) {
        return userService.updateSettings(id, request);
    }

    @Operation(
            summary = "Получить настройки уведомлений пользователя",
            description = "Возвращает текущие настройки email и Telegram уведомлений пользователя"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Настройки уведомлений успешно получены",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = NotificationSettingsResponse.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Настройки уведомлений не найдены",
                    content = @Content(
                            mediaType = "application/problem+json",
                            schema = @Schema(implementation = ErrorResponse.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "Внутренняя ошибка сервера",
                    content = @Content(
                            mediaType = "application/problem+json",
                            schema = @Schema(implementation = ErrorResponse.class)
                    )
            )
    })
    @GetMapping("/{id}/notification-settings")
    public NotificationSettingsResponse getSettings(
            @Parameter(
                    description = "UUID пользователя",
                    required = true,
                    example = "550e8400-e29b-41d4-a716-446655440000"
            )
            @PathVariable UUID id
    ) {
        return userService.getSettings(id);
    }

    @Operation(
            summary = "Изменить роль пользователя",
            description = "Позволяет администратору назначить пользователю новую роль, например SUPPORT"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Роль пользователя успешно изменена",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = UserResponse.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Пользователь не найден",
                    content = @Content(
                            mediaType = "application/problem+json",
                            schema = @Schema(implementation = ErrorResponse.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "403",
                    description = "Недостаточно прав",
                    content = @Content(
                            mediaType = "application/problem+json",
                            schema = @Schema(implementation = ErrorResponse.class)
                    )
            )

    })
    @PatchMapping("/{id}/role")
    public UserResponse updateRole(
            @RequestHeader("X-User-Id") UUID actorUserId,
            @PathVariable UUID id,
            @org.springframework.web.bind.annotation.RequestBody UpdateUserRoleRequest request
    ) {
        return userService.updateRole(actorUserId, id, request);
    }
}

