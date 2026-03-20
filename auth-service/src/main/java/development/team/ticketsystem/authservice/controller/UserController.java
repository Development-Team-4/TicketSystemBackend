package development.team.ticketsystem.authservice.controller;

import development.team.ticketsystem.authservice.dto.notification.NotificationSettingsResponse;
import development.team.ticketsystem.authservice.dto.notification.UpdateNotificationSettingsRequest;
import development.team.ticketsystem.authservice.dto.user.UpdateUserRequest;
import development.team.ticketsystem.authservice.dto.user.UserResponse;
import development.team.ticketsystem.authservice.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
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
@Tag(name = "Users", description = "Пользователи")
public class UserController {

    private final UserService userService;

    @Operation(summary = "Получить пользователя по ID")
    @GetMapping("/{id}")
    public UserResponse getUser(
            @Parameter(description = "ID пользователя", example = "550e8400-e29b-41d4-a716-446655440000")
            @PathVariable UUID id) {
        log.info("Request has been sent to retrieve a user with ID {}", id);
        return userService.getById(id);
    }

    @Operation(summary = "Получить список пользователей")
    @GetMapping
    public List<UserResponse> getUsers() {
        log.info("Request has been sent to retrieve all users");
        return userService.getAll();
    }

    @Operation(summary = "Обновить пользователя")
    @PutMapping("/{id}")
    public UserResponse updateUser(
            @PathVariable UUID id,
            @RequestBody UpdateUserRequest request
    ) {
        log.info("Request has been sent to update the user's data with ID {}", id);
        return userService.update(id, request);
    }

    @Operation(summary = "Обновить настройки уведмолений пользователя")
    @PutMapping("/{id}/notification-settings")
    public NotificationSettingsResponse updateSettings(
            @Parameter(description = "ID пользователя", example = "550e8400-e29b-41d4-a716-446655440000")
            @PathVariable UUID id,
            @RequestBody UpdateNotificationSettingsRequest request
    ) {
        log.info("Request has been sent to update the notification settings for user with ID {}", id);
        return userService.updateSettings(id, request);
    }

    @Operation(summary = "Получить настройки уведмолений пользователя")
    @GetMapping("/{id}/notification-settings")
    public NotificationSettingsResponse getSettings(
            @PathVariable UUID id
    ) {
        log.info("Request has been sent to receive notification settings for user with id {}", id);
        return userService.getSettings(id);
    }

}

