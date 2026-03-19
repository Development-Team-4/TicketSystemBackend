package development.team.ticketsystem.authservice.controller;

import development.team.ticketsystem.authservice.dto.notification.NotificationSettingsResponse;
import development.team.ticketsystem.authservice.dto.notification.UpdateNotificationSettingsRequest;
import development.team.ticketsystem.authservice.dto.user.UpdateUserRequest;
import development.team.ticketsystem.authservice.dto.user.UserResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
@Tag(name = "Users", description = "Пользователи")
public class UserController {

    @Operation(summary = "Получить пользователя по ID")
    @GetMapping("/{id}")
    public UserResponse getUser(
            @Parameter(description = "ID пользователя", example = "550e8400-e29b-41d4-a716-446655440000")
            @PathVariable UUID id) {
        return new UserResponse();
    }

    @Operation(summary = "Получить список пользователей")
    @GetMapping
    public List<UserResponse> getUsers() {
        return new ArrayList<>();
    }

    @Operation(summary = "Обновить пользователя")
    @PutMapping("/{id}")
    public UserResponse updateUser(
            @PathVariable UUID id,
            @RequestBody UpdateUserRequest request
    ) {

        return new UserResponse();
    }

    @Operation(summary = "Обновить настройки уведмолений пользователя")
    @PutMapping("/{id}/notification-settings")
    public NotificationSettingsResponse updateSettings(
            @Parameter(description = "ID пользователя", example = "550e8400-e29b-41d4-a716-446655440000")
            @PathVariable UUID id,
            @RequestBody UpdateNotificationSettingsRequest request
    ) {
        return new NotificationSettingsResponse();
    }

    @Operation(summary = "Получить настройки уведмолений пользователя")
    @GetMapping("/{id}/notification-settings")
    public NotificationSettingsResponse getSettings(
            @PathVariable UUID id
    ) {
        return new NotificationSettingsResponse();
    }

}

