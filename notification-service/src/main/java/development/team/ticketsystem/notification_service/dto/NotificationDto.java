package development.team.ticketsystem.notification_service.dto;

import development.team.ticketsystem.notification_service.entity.NotificationType;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.util.UUID;

@Data
@RequiredArgsConstructor
public class NotificationDto {
    private final UUID userId;
    private final UUID ticketId;
    private final NotificationType type;
}
