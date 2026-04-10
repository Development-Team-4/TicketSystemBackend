package development.team.ticketsystem.ticketservice.mappers;

import development.team.ticketsystem.ticketservice.dto.audit.TicketAuditResponse;
import development.team.ticketsystem.ticketservice.entity.TicketAuditEntity;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface TicketAuditMapper {

    TicketAuditResponse toDto(TicketAuditEntity entity);

    List<TicketAuditResponse> toDtoList(List<TicketAuditEntity> entities);
}