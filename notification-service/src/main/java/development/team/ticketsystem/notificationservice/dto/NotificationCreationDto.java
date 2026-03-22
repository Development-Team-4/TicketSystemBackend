package development.team.ticketsystem.notificationservice.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import development.team.ticketsystem.notificationservice.entity.NotificationType;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.UUID;

@Data
@AllArgsConstructor
public class NotificationCreationDto {
    @NotNull(message = "userId не может быть null")
    @JsonProperty(value = "user_id")
    @Schema(description = "ID пользователя", required = true)
    private UUID userId;

    @NotNull(message = "ticketId не может быть null")
    @JsonProperty(value = "ticket_id")
    @Schema(description = "ID тикета", required = true)
    private UUID ticketId;

    @NotNull(message = "userId не может быть null")
    @JsonProperty(value = "type")
    @Schema(description = "Тип тикета", required = true)
    private NotificationType type;
}
