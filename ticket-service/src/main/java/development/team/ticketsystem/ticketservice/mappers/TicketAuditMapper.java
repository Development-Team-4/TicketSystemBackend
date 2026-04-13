package development.team.ticketsystem.ticketservice.mappers;

import development.team.ticketsystem.ticketservice.dto.audit.TicketAuditResponse;
import development.team.ticketsystem.ticketservice.entity.TicketAuditEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface TicketAuditMapper {

    TicketAuditResponse toResponse(TicketAuditEntity entity);

}