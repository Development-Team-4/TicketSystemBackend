package development.team.ticketsystem.notification_service.service;

import development.team.ticketsystem.notification_service.dto.NotificationDto;
import development.team.ticketsystem.notification_service.entity.Notification;
import development.team.ticketsystem.notification_service.repository.NotificationRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class NotificationService {
    private final NotificationRepository notificationRepository;

    /**
     * Метод выдачи всех нотификаций
     *
     * @return список нотификаций
     */
    public List<Notification> getAllNotifications() {
        log.info("Идёт поиск всех уведомлений в БД");

        List<Notification> result = this.notificationRepository.findAll();

        log.info("Результат поиска: {}", result);

        return result;
    }

    // В дальнейшем поменяю DTO, в котором ещё будет передаваться settings
    /**
     * Метод добавления нового уведомления через DTO
     *
     * @param dto DTO для создания уведомления
     */
    public boolean addNewNotification(NotificationDto dto) {
        log.info("Начат процесс добавления нового уведомления в БД: {}", dto);

        try {
            Notification notification = new Notification(
                    dto.getUserId(),
                    dto.getTicketId(),
                    dto.getType()
            );

            this.notificationRepository.save(notification);

            return true;
        } catch(Exception ex) {
            log.error("Информация не добавлена, возникла ошибка");

            return false;
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
        log.info("Идёт поиск всех уведомлений конкретного пользователя в БД");

        List<Notification> result = this.notificationRepository.findByUserId(userId);

        log.info("Результат поиска: {}", result);

        return result;
    }

    /**
     * Метод для отправки сообщения по email
     * (ПОКА НЕ РЕАЛИЗОВАН)
     */
    @Deprecated
    private void sendEmailMessage() {

    }

    /**
     * Метод для отправки сообщения в telegram
     * (ПОКА НЕ РЕАЛИЗОВАН)
     */
    @Deprecated
    private void sendTelegramMessage() {

    }
}
