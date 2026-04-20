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

import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class NotificationService {

    private final JavaMailSender mailSender;

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

    /**
     * Метод добавления нового уведомления через DTO
     *
     * @param dto DTO для создания уведомления
     * @return dto с данными уведомления
     */
    public NotificationDto addNewNotification(NotificationCreationDto dto) throws NotificationFormatException {
        log.info("Creating notification: userId={}, ticketId={}, type={}",
                dto.getUserId(), dto.getTicketId(), dto.getType());

        Notification notification = this.notificationMapper.toEntity(dto);

        notification.setTitle(this.getTitle(dto.getType()));
        notification.setMessage(this.getMessage(dto.getType()));

        log.debug("Notification content prepared: title='{}', message='{}'",
                notification.getTitle(), notification.getMessage());

        Notification saved = this.notificationRepository.save(notification);

        log.info("Notification saved: id={}, userId={}",
                saved.getId(), saved.getUserId());

        try {
            log.info("Attempting to send Telegram notification: notificationId={}, userId={}",
                    saved.getId(), saved.getUserId());

            sendTelegramMessage(saved);

            log.info("Telegram notification flow finished: notificationId={}", saved.getId());

            sendEmailMessage(saved);
        } catch (Exception e) {
            log.error("Telegram notification failed: notificationId={}, userId={}",
                    saved.getId(), saved.getUserId(), e);
        }

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

    private String getTitle(NotificationType type) {
        return this.notificationContentProvider.getTemplate(type.name()).getTitle();
    }

    private String getMessage(NotificationType type) {
        return this.notificationContentProvider.getTemplate(type.name()).getMessage();
    }

    private void sendTelegramMessage(Notification notification) {
        try {
            var settings = authServiceClient.getNotificationSettings(notification.getUserId());

            if (settings == null) {
                log.warn("No settings for userId={}", notification.getUserId());
                return;
            }

            String chatIdStr = settings.userTelegramNotification();

            if (chatIdStr == null || chatIdStr.isBlank()) {
                log.warn("Telegram not enabled for userId={}", notification.getUserId());
                return;
            }

            Long chatId = Long.parseLong(chatIdStr);

            String ticketUrl = "https://ticketsystem.braverto.com/tickets/" + notification.getTicketId();

            String text = "<b><i>" + notification.getMessage() + "</i></b>\n";

            botServiceClient.sendTelegramMessage(chatId, text, ticketUrl);

            log.info("Telegram sent to chatId={}", chatId);

        } catch (Exception exception) {
            log.error("Failed to send telegram for userId={}", notification.getUserId(), exception);
        }
    }

    private void sendEmailMessage(Notification notification) {
        try {
            var settings = authServiceClient.getNotificationSettings(notification.getUserId());

            if (settings == null) {
                log.warn("No settings for userId={}", notification.getUserId());
                return;
            }

            String email = settings.userEmailNotification();

            if (email == null || email.isBlank()) {
                log.warn("Email not enabled for userId={}", notification.getUserId());
                return;
            }

            String ticketUrl = "https://ticketsystem.braverto.com/tickets/" + notification.getTicketId();

            SimpleMailMessage message = new SimpleMailMessage();
            message.setTo(email);

            message.setFrom("bravertogroup@gmail.com");

            message.setSubject(notification.getTitle());
            message.setText(
                    notification.getMessage()
                            + "\n\nOpen ticket: " + ticketUrl
            );

            mailSender.send(message);

            log.info("Email sent to {}", email);

        } catch (Exception exception) {
            log.error("Failed to send email for userId={}", notification.getUserId(), exception);
        }
    }
}