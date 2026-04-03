package development.team.ticketsystem.notificationservice.mapper;

import development.team.ticketsystem.notificationservice.dto.NotificationCreationDto;
import development.team.ticketsystem.notificationservice.entity.Notification;
import org.mapstruct.Mapper;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.List;

import development.team.ticketsystem.notificationservice.dto.NotificationDto;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface NotificationMapper {
    NotificationDto toDto(Notification notification);

    List<NotificationDto> toDtoList(List<Notification> notifications);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "sent", constant = "true")
    Notification toEntity(NotificationCreationDto notificationDTO);
}