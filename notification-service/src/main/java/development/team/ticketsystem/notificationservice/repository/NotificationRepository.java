package development.team.ticketsystem.notificationservice.repository;

import development.team.ticketsystem.notificationservice.entity.Notification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface NotificationRepository extends JpaRepository<Notification, UUID> {
    List<Notification> findByUserId(UUID userId);

    void deleteAllByUserId(UUID userId);

    void deleteByUserIdAndId(UUID userId, UUID id);
}
