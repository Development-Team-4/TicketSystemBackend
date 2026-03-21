package development.team.ticketsystem.authservice.service;

import development.team.ticketsystem.authservice.dto.notification.NotificationSettingsResponse;
import development.team.ticketsystem.authservice.dto.notification.UpdateNotificationSettingsRequest;
import development.team.ticketsystem.authservice.dto.user.UpdateUserRequest;
import development.team.ticketsystem.authservice.dto.user.UserResponse;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public interface UserService {

    UserResponse getById(UUID id);

    List<UserResponse> getAll();

    UserResponse update(UUID id, UpdateUserRequest request);

    NotificationSettingsResponse getSettings(UUID id);

    NotificationSettingsResponse updateSettings(UUID id, UpdateNotificationSettingsRequest request);

}
