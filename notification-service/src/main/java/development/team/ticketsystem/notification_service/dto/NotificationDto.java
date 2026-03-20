package development.team.ticketsystem.notification_service.dto;

import development.team.ticketsystem.notification_service.entity.NotificationType;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.UUID;

@Data
@AllArgsConstructor
public class NotificationDto {
    @NotNull(message = "userId не может быть null")
    private UUID userId;

    @NotNull(message = "ticketId не может быть null")
    private UUID ticketId;

    @NotNull(message = "userId не может быть null")
    private NotificationType type;
}
