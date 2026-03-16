package development.team.ticketsystem.auth_service.controller;

import development.team.ticketsystem.auth_service.dto.notification.NotificationSettingsResponse;
import development.team.ticketsystem.auth_service.dto.notification.UpdateNotificationSettingsRequest;
import development.team.ticketsystem.auth_service.mapper.NotificationSettingsMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/users/{userId}/notification-settings")
@RequiredArgsConstructor
public class NotificationSettingsController {

    @GetMapping
    public NotificationSettingsResponse getSettings(
            @PathVariable UUID userId
    ) {
        return new NotificationSettingsResponse();
    }

    @PutMapping
    public NotificationSettingsResponse updateSettings(
            @PathVariable UUID userId,
            @RequestBody UpdateNotificationSettingsRequest request
    ) {
        return new NotificationSettingsResponse();
    }

}
