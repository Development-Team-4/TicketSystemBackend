package development.team.ticketsystem.authservice.service;

import development.team.ticketsystem.authservice.dto.notification.NotificationSettingsResponse;
import development.team.ticketsystem.authservice.dto.notification.UpdateNotificationSettingsRequest;
import development.team.ticketsystem.authservice.dto.user.UpdateUserRequest;
import development.team.ticketsystem.authservice.dto.user.UserResponse;
import development.team.ticketsystem.authservice.entity.User;
import development.team.ticketsystem.authservice.entity.UserNotificationSettings;
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
                .orElseThrow(() -> new RuntimeException("Settings not found"));

        return mapper.toResponse(settings);
    }

    @Transactional
    public NotificationSettingsResponse updateSettings(UUID id, UpdateNotificationSettingsRequest request) {
        User user = getUserOrThrow(id);

        UserNotificationSettings settings = settingsRepository.findById(id)
                .orElseGet(() -> UserNotificationSettings.builder()
                        .user(user)
                        .userId(user.getId())
                        .build());

        settings.setEmailEnabled(request.getEmailEnabled());
        settings.setTelegramEnabled(request.getTelegramEnabled());

        return mapper.toResponse(settingsRepository.save(settings));
    }

    private User getUserOrThrow(UUID id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));
    }
}