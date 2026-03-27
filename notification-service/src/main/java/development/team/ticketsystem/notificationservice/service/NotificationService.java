package development.team.ticketsystem.notificationservice.service;

import development.team.ticketsystem.notificationservice.dto.NotificationCreationDto;
import development.team.ticketsystem.notificationservice.dto.NotificationDto;
import development.team.ticketsystem.notificationservice.entity.Notification;
import development.team.ticketsystem.notificationservice.exceptions.NotificationFormatException;
import development.team.ticketsystem.notificationservice.mapper.NotificationMapper;
import development.team.ticketsystem.notificationservice.repository.NotificationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class NotificationService {
    private final NotificationRepository notificationRepository;

    private final NotificationMapper notificationMapper;

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
        Notification notification = createNotificationFromDto(dto);

        return notificationMapper.toDto(this.notificationRepository.save(notification));
    }

    /**
     * Перегруженный метод выдачи всех нотификаций
     * (для конкретного пользователя)
     *
     * @param userId user_id пользователя
     * @return список уведомлений
     */
    public List<NotificationDto> getAllNotifications(UUID userId) {
        List<Notification> result = this.notificationRepository.findByUserId(userId);

        return notificationMapper.toDtoList(result);
    }

    /**
     * Метод для отправки сообщения по email
     * (ПОКА НЕ РЕАЛИЗОВАН)
     */
    private void sendEmailMessage() {
        throw new UnsupportedOperationException();
    }

    /**
     * Метод для отправки сообщения в telegram
     * (ПОКА НЕ РЕАЛИЗОВАН)
     */
    private void sendTelegramMessage() {
        throw new UnsupportedOperationException();
    }

    /**
     * Создание сущности уведомления из DTO
     *
     * @param dto DTO для создания уведомления
     */
    private Notification createNotificationFromDto(NotificationCreationDto dto) throws NotificationFormatException {
        if (dto.getUserId() == null) {
            throw new NotificationFormatException("userId не может быть null");
        }
        if (dto.getTicketId() == null) {
            throw new NotificationFormatException("ticketId не может быть null");
        }
        if (dto.getType() == null) {
            throw new NotificationFormatException("notification не может быть null");
        }

        return new Notification(
                dto.getUserId(),
                dto.getTicketId(),
                dto.getType()
        );
    }
}
