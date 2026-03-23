package development.team.ticketsystem.notificationservice.mapper;

import development.team.ticketsystem.notificationservice.entity.Notification;
import org.mapstruct.Mapper;
import java.util.List;
import development.team.ticketsystem.notificationservice.dto.NotificationDto;

@Mapper(componentModel = "spring")
public interface NotificationMapper {

    NotificationDto toDto(Notification notification);

    List<NotificationDto> toDtoList(List<Notification> notifications);

    Notification toEntity(NotificationDto notificationDTO);
}