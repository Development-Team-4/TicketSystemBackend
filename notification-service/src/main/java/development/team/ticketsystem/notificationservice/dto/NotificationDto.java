package development.team.ticketsystem.notificationservice.dto;

import development.team.ticketsystem.notificationservice.entity.NotificationType;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.UUID;

@Data
@AllArgsConstructor
public class NotificationDto {
    //@NotNull(message = "userId не может быть null")
    private UUID userId;

    //@NotNull(message = "ticketId не может быть null")
    private UUID ticketId;

    //@NotNull(message = "userId не может быть null")
    private NotificationType type;
}
