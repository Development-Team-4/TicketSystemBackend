package development.team.ticketsystem.authservice.controller;

import development.team.ticketsystem.authservice.dto.notification.NotificationSettingsResponse;
import development.team.ticketsystem.authservice.repository.UserNotificationSettingsRepository;
import io.swagger.v3.oas.annotations.Hidden;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@Hidden
@RestController
@RequestMapping("/internal/users")
@RequiredArgsConstructor
public class InternalUserController {

    private final UserNotificationSettingsRepository repository;

    @GetMapping("/{userId}/notification-settings")
    public NotificationSettingsResponse getNotificationSettings(@PathVariable UUID userId) {
        var user = repository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        return new NotificationSettingsResponse(
                user.getTelegramEnabled(),
                user.getEmailEnabled()
        );
    }
}