package development.team.ticketsystem.authservice.repository;

import development.team.ticketsystem.authservice.entity.UserNotificationSettings;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface UserNotificationSettingsRepository extends JpaRepository<UserNotificationSettings, UUID> {
}
