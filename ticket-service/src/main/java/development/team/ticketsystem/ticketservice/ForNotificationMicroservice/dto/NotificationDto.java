package development.team.ticketsystem.ticketservice.ForNotificationMicroservice.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.sql.Timestamp;
import java.util.UUID;

@Data
@RequiredArgsConstructor
public class NotificationDto {
    @JsonProperty(value = "id")
    @NotBlank
    @NotNull
    private UUID id;

    @JsonProperty(value = "userId")
    @NotBlank
    @NotNull
    private UUID userId;

    @JsonProperty(value = "ticketId")
    @NotBlank
    @NotNull
    private UUID ticketId;

    @JsonProperty(value = "type")
    @NotNull
    private NotificationType type;

    @JsonProperty(value = "title")
    private String title;

    @JsonProperty(value = "message")
    private String message;

    @JsonProperty(value = "sent")
    private Boolean sent;

    @JsonProperty(value = "createdAt")
    private Timestamp createdAt;

    @JsonProperty(value = "updatedAt")
    private Timestamp updatedAt;
}
