package development.team.ticketsystem.notification_service.service;

import development.team.ticketsystem.notification_service.dto.NotificationDto;
import development.team.ticketsystem.notification_service.entity.Notification;
import development.team.ticketsystem.notification_service.repository.NotificationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class NotificationService {
    private final NotificationRepository notificationRepository;

    /**
     * Метод выдачи всех нотификаций
     *
     * @return список нотификаций
     */
    public List<Notification> getAllNotifications() {
        return this.notificationRepository.findAll();
    }

    // В дальнейшем поменяю DTO, в котором ещё будет передаваться settings
    /**
     * Метод добавления нового уведомления через DTO
     *
     * @param dto DTO для создания уведомления
     */
    public ResponseEntity<?> addNewNotification(NotificationDto dto) {
        try {
            Notification notification = new Notification(
                    dto.getUserId(),
                    dto.getTicketId(),
                    dto.getType()
            );

            this.notificationRepository.save(notification);

            return ResponseEntity.ok().build();
        } catch(Exception ex) {
            return ResponseEntity.badRequest().build();
        }
    }

    /**
     * Перегруженный метод выдачи всех нотификаций
     * (для конкретного пользователя)
     *
     * @param userId user_id пользователя
     * @return список уведомлений
     */
    public List<Notification> getAllNotifications(UUID userId) {
        return this.notificationRepository.findByUserId(userId);
    }

    /**
     *
     */
    @Deprecated
    private void sendEmailMessage() {

    }

    /**
     *
     */
    @Deprecated
    private void sendTelegramMessage() {

    }
}
