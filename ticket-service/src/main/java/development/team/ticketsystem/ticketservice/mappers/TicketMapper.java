package development.team.ticketsystem.ticketservice.mappers;

import development.team.ticketsystem.ticketservice.dto.tickets.CreateTicketRequest;
import development.team.ticketsystem.ticketservice.dto.tickets.TicketResponse;
import development.team.ticketsystem.ticketservice.dto.tickets.UpdateTicketRequest;
import development.team.ticketsystem.ticketservice.entity.TicketEntity;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface TicketMapper {

    TicketResponse toResponse(TicketEntity entity);

    TicketEntity toEntity(CreateTicketRequest request);

    void updateEntityFromRequest(UpdateTicketRequest request, @MappingTarget TicketEntity entity);
}