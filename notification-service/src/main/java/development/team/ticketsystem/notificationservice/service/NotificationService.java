package development.team.ticketsystem.notificationservice.service;

import development.team.ticketsystem.notificationservice.dto.NotificationDto;
import development.team.ticketsystem.notificationservice.entity.Notification;
import development.team.ticketsystem.notificationservice.exceptions.NotificationFormatException;
import development.team.ticketsystem.notificationservice.repository.NotificationRepository;
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
    public Notification addNewNotification(NotificationDto dto) throws NotificationFormatException {
        log.info("Начат процесс добавления нового уведомления в БД: {}", dto);

        if (dto.getType() == null) {
            throw new NotificationFormatException("type не может быть null");
        }

        if (dto.getUserId() == null) {
            throw new NotificationFormatException("userId не может быть null");
        }

        if (dto.getTicketId() == null) {
            throw new NotificationFormatException("ticketId не может быть null");
        }

        Notification notification = new Notification(
                dto.getUserId(),
                dto.getTicketId(),
                dto.getType()
        );

        return this.notificationRepository.save(notification);
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
