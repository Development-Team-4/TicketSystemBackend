package development.team.ticketsystem.authservice.mapper;

import development.team.ticketsystem.authservice.dto.notification.NotificationSettingsResponse;
import development.team.ticketsystem.authservice.dto.user.UserResponse;
import development.team.ticketsystem.authservice.entity.User;
import development.team.ticketsystem.authservice.entity.UserNotificationSettings;
import org.springframework.stereotype.Component;

@Component
public class UserMapper {

    public UserResponse toResponse(User user) {
        return new UserResponse(
                user.getId(),
                user.getName(),
                user.getEmail(),
                user.getRole(),
                user.getAvatar(),
                user.getCreatedAt()
        );
    }

    public NotificationSettingsResponse toResponse(UserNotificationSettings settings) {
        return new NotificationSettingsResponse(
                settings.getEmailEnabled(),
                settings.getTelegramEnabled()
        );
    }
}
