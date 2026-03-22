package development.team.ticketsystem.notificationservice.mapper;

import development.team.ticketsystem.notificationservice.entity.Notification;
import org.mapstruct.Mapper;
import java.util.List;
import development.team.ticketsystem.notificationservice.dto.NotificationDto;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface NotificationMapper {

    @Mapping(source = "id", target = "id")
    @Mapping(source = "userId", target = "userId")
    @Mapping(source = "ticketId", target = "ticketId")
    @Mapping(source = "type", target = "type")
    @Mapping(source = "title", target = "title")
    @Mapping(source = "message", target = "message")
    @Mapping(source = "sent", target = "sent")
    @Mapping(source = "createdAt", target = "createdAt")
    @Mapping(source = "updatedAt", target = "updatedAt")
    NotificationDto toDto(Notification notification);

    List<NotificationDto> toDtoList(List<Notification> notifications);

    Notification toEntity(NotificationDto notificationDTO);
}