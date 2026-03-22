package development.team.ticketsystem.notificationservice.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import development.team.ticketsystem.notificationservice.entity.NotificationType;
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

    @JsonProperty(value = "user_id")
    @NotBlank
    @NotNull
    private UUID userId;

    @JsonProperty(value = "ticket_id")
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

    @JsonProperty(value = "created_at")
    private Timestamp createdAt;

    @JsonProperty(value = "updated_at")
    private Timestamp updatedAt;
}
