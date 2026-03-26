package development.team.ticketsystem.ticketservice.ForNotificationMicroservice.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import development.team.ticketsystem.ticketservice.ForNotificationMicroservice.dto.NotificationType;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.UUID;

@Data
@AllArgsConstructor
public class NotificationCreationDto {
    @NotNull(message = "userId не может быть null")
    @JsonProperty(value = "userId")
    @Schema(description = "ID пользователя", requiredMode = Schema.RequiredMode.REQUIRED)
    private UUID userId;

    @NotNull(message = "ticketId не может быть null")
    @JsonProperty(value = "ticketId")
    @Schema(description = "ID тикета", requiredMode = Schema.RequiredMode.REQUIRED)
    private UUID ticketId;

    @NotNull(message = "userId не может быть null")
    @JsonProperty(value = "type")
    @Schema(description = "Тип тикета", requiredMode = Schema.RequiredMode.REQUIRED)
    private NotificationType type;
}
