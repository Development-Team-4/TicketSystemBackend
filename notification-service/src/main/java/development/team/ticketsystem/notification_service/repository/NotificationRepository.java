package development.team.ticketsystem.notification_service.repository;

import development.team.ticketsystem.notification_service.entity.Notification;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface NotificationRepository extends JpaRepository<Notification, UUID> {
}
