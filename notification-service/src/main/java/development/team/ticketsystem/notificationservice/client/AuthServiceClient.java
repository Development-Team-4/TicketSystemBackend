package development.team.ticketsystem.notificationservice.client;

import development.team.ticketsystem.notificationservice.dto.InternalNotificationSettingsResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

import java.util.UUID;

@Component
@RequiredArgsConstructor
public class AuthServiceClient {

    private final RestClient authRestClient;

    public InternalNotificationSettingsResponse getNotificationSettings(UUID userId) {
        return authRestClient.get()
                .uri("/internal/users/{userId}/notification-settings", userId)
                .retrieve()
                .body(InternalNotificationSettingsResponse.class);
    }
}