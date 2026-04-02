package development.team.ticketsystem.authservice.service;

import development.team.ticketsystem.authservice.dto.notification.NotificationSettingsResponse;
import development.team.ticketsystem.authservice.dto.notification.UpdateNotificationSettingsRequest;
import development.team.ticketsystem.authservice.dto.user.UpdateUserRequest;
import development.team.ticketsystem.authservice.dto.user.UpdateUserRoleRequest;
import development.team.ticketsystem.authservice.dto.user.UserResponse;
import development.team.ticketsystem.authservice.entity.User;
import development.team.ticketsystem.authservice.entity.UserNotificationSettings;
import development.team.ticketsystem.authservice.exception.CannotChangeOwnRoleException;
import development.team.ticketsystem.authservice.exception.NotificationSettingsNotFoundException;
import development.team.ticketsystem.authservice.exception.UserNotFoundException;
import development.team.ticketsystem.authservice.mapper.UserMapper;
import development.team.ticketsystem.authservice.repository.UserNotificationSettingsRepository;
import development.team.ticketsystem.authservice.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final UserNotificationSettingsRepository settingsRepository;
    private final UserMapper mapper;

    public UserResponse getById(UUID id) {
        return mapper.toResponse(getUserOrThrow(id));
    }

    public List<UserResponse> getAll() {
        return userRepository.findAll()
                .stream()
                .map(mapper::toResponse)
                .toList();
    }

    @Transactional
    public UserResponse update(UUID id, UpdateUserRequest request) {
        User user = getUserOrThrow(id);

        if (request.getName() != null) {
            user.setName(request.getName());
        }

        if (request.getAvatar() != null) {
            user.setAvatar(request.getAvatar());
        }

        return mapper.toResponse(userRepository.save(user));
    }

    public NotificationSettingsResponse getSettings(UUID id) {
        UserNotificationSettings settings = settingsRepository.findById(id)
                .orElseThrow(NotificationSettingsNotFoundException::new);

        return mapper.toResponse(settings);
    }

    @Transactional
    public NotificationSettingsResponse updateSettings(UUID id, UpdateNotificationSettingsRequest request) {
        User user = getUserOrThrow(id);

        UserNotificationSettings settings = settingsRepository.findById(id)
                .orElseGet(() -> {
                    UserNotificationSettings newSettings = new UserNotificationSettings();
                    newSettings.setUser(user);
                    return newSettings;
                });

        settings.setEmailEnabled(request.getEmailEnabled());
        settings.setTelegramEnabled(request.getTelegramEnabled());

        return mapper.toResponse(settingsRepository.save(settings));
    }

    @Transactional
    public UserResponse updateRole(UUID actorUserId, UUID targetUserId, UpdateUserRoleRequest request) {
        if (actorUserId.equals(targetUserId)) {
            throw new CannotChangeOwnRoleException();
        }

        User user = getUserOrThrow(targetUserId);
        user.setRole(request.getRole());

        return mapper.toResponse(userRepository.save(user));
    }
    private User getUserOrThrow(UUID id) {
        return userRepository.findById(id)
                .orElseThrow(UserNotFoundException::new);
    }
}