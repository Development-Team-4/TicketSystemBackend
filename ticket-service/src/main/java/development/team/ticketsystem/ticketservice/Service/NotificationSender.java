package development.team.ticketsystem.ticketservice.Service;

import development.team.ticketsystem.ticketservice.ForNotificationMicroservice.dto.NotificationCreationDto;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

import java.util.UUID;


@Component
public class NotificationSender {
    private final RestClient restClient;

    public NotificationSender() {
        this.restClient = RestClient.create();
    }

    public void sendToNotificationMicroservice(
            UUID toUserId,
            NotificationCreationDto request
    ) {
        restClient.post()
                .uri("http://localhost:8083/notifications/"+toUserId.toString())
                .contentType(MediaType.APPLICATION_JSON)
                .body(request);
    }
}

