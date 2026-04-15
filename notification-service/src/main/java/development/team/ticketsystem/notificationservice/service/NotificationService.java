package development.team.ticketsystem.notificationservice.service;

import development.team.ticketsystem.notificationservice.client.AuthServiceClient;
import development.team.ticketsystem.notificationservice.client.BotServiceClient;
import development.team.ticketsystem.notificationservice.config.NotificationContentProvider;
import development.team.ticketsystem.notificationservice.dto.NotificationCreationDto;
import development.team.ticketsystem.notificationservice.dto.NotificationDto;
import development.team.ticketsystem.notificationservice.entity.Notification;
import development.team.ticketsystem.notificationservice.entity.NotificationType;
import development.team.ticketsystem.notificationservice.exceptions.NotificationNotFoundException;
import development.team.ticketsystem.notificationservice.exceptions.UserNotFoundException;
import development.team.ticketsystem.notificationservice.exceptions.NotificationFormatException;
import development.team.ticketsystem.notificationservice.mapper.NotificationMapper;
import development.team.ticketsystem.notificationservice.repository.NotificationRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class NotificationService {

    private final AuthServiceClient authServiceClient;
    private final BotServiceClient botServiceClient;

    private final NotificationRepository notificationRepository;

    private final NotificationMapper notificationMapper;

    private final NotificationContentProvider notificationContentProvider;

    /**
     * Метод выдачи всех нотификаций
     *
     * @return список нотификаций
     */
    public List<NotificationDto> getAllNotifications() {
        List<Notification> result = this.notificationRepository.findAll();

        return this.notificationMapper.toDtoList(result);
    }

    // В дальнейшем поменяю DTO, в котором ещё будет передаваться settings
    /**
     * Метод добавления нового уведомления через DTO
     *
     * @param dto DTO для создания уведомления
     * @return dto с данными уведомления
     */
    public NotificationDto addNewNotification(NotificationCreationDto dto) throws NotificationFormatException {
        Notification notification = this.notificationMapper.toEntity(dto);

        notification.setTitle(this.getTitle(dto.getType()));
        notification.setMessage(this.getMessage(dto.getType()));

        Notification saved = this.notificationRepository.save(notification);

        sendTelegramMessage(saved);

        return notificationMapper.toDto(saved);
    }

    /**
     * Перегруженный метод выдачи всех нотификаций
     * (для конкретного пользователя)
     *
     * @param userId userId пользователя
     * @return список уведомлений
     */
    public List<NotificationDto> getAllNotifications(UUID userId) {
        List<Notification> result = this.notificationRepository.findByUserId(userId);

        return notificationMapper.toDtoList(result);
    }

    /**
     * Метод удаления всех уведомлений пользователя
     * @param userId ID пользователя
     */
    @Transactional
    public void deleteAllUserNotifications(UUID userId) throws UserNotFoundException {
        List<Notification> notifications = this.notificationRepository.findByUserId(userId);

        if(notifications.isEmpty()) {
            throw new UserNotFoundException(userId);
        }

        this.notificationRepository.deleteAllByUserId(userId);
    }

    /**
     * Метод удаления уведомления
     * @param userId ID пользователя
     * @param notificationId ID уведомления
     */
    @Transactional
    public void deleteUserNotificationById(UUID userId, UUID notificationId) {
        this.notificationRepository.deleteByUserIdAndId(userId, notificationId);
    }

    /**
     * Метод установка флага "sent" в Notification в true
     * @param notificationId ID уведомления
     * @throws NotificationNotFoundException если уведомление не найдено
     */
    @Transactional
    public void setReadStatusToNotification(UUID notificationId) throws NotificationNotFoundException {
        Optional<Notification> notificationOptional = this.notificationRepository.findById(notificationId);

        if(notificationOptional.isEmpty()) {
            throw new NotificationNotFoundException(notificationId);
        }

        Notification notification = notificationOptional.get();
        notification.setRead(true);

        this.notificationRepository.save(notification);
    }

    /**
     * Метод для отправки сообщения по email
     * (ПОКА НЕ РЕАЛИЗОВАН)
     */
    private void sendEmailMessage() {
        throw new UnsupportedOperationException();
    }

    private String getTitle(NotificationType type) {
        return this.notificationContentProvider.getTemplate(type.name()).getTitle();
    }

    private String getMessage(NotificationType type) {
        return this.notificationContentProvider.getTemplate(type.name()).getMessage();
    }

    private void sendTelegramMessage(Notification notification) {
        try {
            var settings = authServiceClient.getNotificationSettings(notification.getUserId());

            log.debug("tg:{}", settings.telegramNotification());
            if (settings == null
                    || settings.telegramNotification() == null
                    || settings.telegramNotification().isBlank()) {
                return;
            }

            Long chatId = Long.parseLong(settings.telegramNotification());
            log.debug("tg:{}", chatId);
            String text = "<b>" + notification.getTitle() + "</b>\n" +
                    notification.getMessage();

            botServiceClient.sendTelegramMessage(chatId, text);

        } catch (Exception exception) {
            throw new RuntimeException("ERROR");
        }
    }
}