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
@Schema(description = "DTO для создания уведомления")
public class NotificationCreationDto {
    @NotNull(message = "userId не может быть null")
    @JsonProperty(value = "userId")
    @Schema(
            description = "ID пользователя",
            example = "550e8400-e29b-41d4-a716-446655440001",
            requiredMode = Schema.RequiredMode.REQUIRED
    )
    private UUID userId;

    @NotNull(message = "ticketId не может быть null")
    @JsonProperty(value = "ticketId")
    @Schema(
            description = "ID тикета",
            example = "550e8400-e29b-41d4-a716-446655440001",
            requiredMode = Schema.RequiredMode.REQUIRED
    )
    private UUID ticketId;

    @NotNull(message = "userId не может быть null")
    @JsonProperty(value = "type")
    @Schema(
            description = "ID пользователя",
            example = "COMMENT",
            requiredMode = Schema.RequiredMode.REQUIRED
    )
    private NotificationType type;
}
