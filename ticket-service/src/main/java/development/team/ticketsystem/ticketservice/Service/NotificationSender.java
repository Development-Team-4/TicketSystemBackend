package development.team.ticketsystem.ticketservice.Service;

import development.team.ticketsystem.ticketservice.ForNotificationMicroservice.dto.NotificationCreationDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

import java.util.UUID;


@Component
@RequiredArgsConstructor
public class NotificationSender {
    private final RestClient restClient = RestClient.create();
    private static final String BASE_URL = "http://localhost:8083/notifications/";

    public void sendToNotificationMicroservice(
            UUID toUserId,
            NotificationCreationDto request
    ) {
        restClient.post()
                .uri(BASE_URL + toUserId.toString())
                .contentType(MediaType.APPLICATION_JSON)
                .body(request);
    }
}

