package development.team.ticketsystem.ticketservice.service;

import development.team.ticketsystem.ticketservice.exceptions.NotificationServiceException;
import development.team.ticketsystem.ticketservice.forNotificationMicroservice.NotificationCreationDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClientException;

import java.util.UUID;

@Slf4j
@Component
@RequiredArgsConstructor
public class NotificationSender {

    private final RestClient restClient;

    @Value("${services.notification.url:http://localhost:8083}")
    private String notificationServiceUrl;

    public void sendToNotificationMicroservice(
            UUID toUserId,
            NotificationCreationDto request
    ) throws NotificationServiceException {
        String url = notificationServiceUrl + "/notifications";

        try {
            restClient.post()
                    .uri(url)
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(request)
                    .retrieve()
                    .toBodilessEntity();

        } catch (RestClientException e) {
            throw new NotificationServiceException(
                    String.format("Failed to send notification to user %s", toUserId), e
            );
        }
    }
}

