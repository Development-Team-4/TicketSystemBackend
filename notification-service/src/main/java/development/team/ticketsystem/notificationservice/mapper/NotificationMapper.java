package development.team.ticketsystem.notificationservice.mapper;

import development.team.ticketsystem.notificationservice.dto.NotificationCreationDto;
import development.team.ticketsystem.notificationservice.entity.Notification;
import development.team.ticketsystem.notificationservice.entity.NotificationType;
import development.team.ticketsystem.notificationservice.service.MessageFormatter;
import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.List;

import development.team.ticketsystem.notificationservice.dto.NotificationDto;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Mapper(componentModel = "spring")
@Component
abstract public class NotificationMapper {
    @Autowired
    private MessageFormatter formatter;

    public abstract NotificationDto toDto(Notification notification);

    public abstract List<NotificationDto> toDtoList(List<Notification> notifications);

    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    public abstract Notification toEntity(NotificationCreationDto notificationDTO);

    @AfterMapping
    protected void setTimestamps(@MappingTarget Notification notification) {
        if (notification.getCreatedAt() == null) {
            Timestamp now = Timestamp.from(Instant.now());
            notification.setCreatedAt(now);
            notification.setUpdatedAt(now);
        }
    }

    @AfterMapping
    protected void setLocalizedFields(@MappingTarget Notification notification, NotificationType type) {
        if (type != null) {
            notification.setTitle(formatter.getTitleForNotification(type));
            notification.setMessage(formatter.getMessageForNotification(type));
        }
        setTimestamps(notification);
    }
}