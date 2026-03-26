package development.team.ticketsystem.ticketservice.Mappers;

import development.team.ticketsystem.ticketservice.DTO.tickets.CreateTicketRequest;
import development.team.ticketsystem.ticketservice.DTO.tickets.TicketResponse;
import development.team.ticketsystem.ticketservice.DTO.tickets.UpdateTicketRequest;
import development.team.ticketsystem.ticketservice.Entity.TicketEntity;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface TicketMapper {

    TicketMapper INSTANCE = Mappers.getMapper(TicketMapper.class);

    TicketResponse toResponse(TicketEntity entity);

    TicketEntity toEntity(CreateTicketRequest request);

    void updateEntityFromRequest(UpdateTicketRequest request, @MappingTarget TicketEntity entity);
}