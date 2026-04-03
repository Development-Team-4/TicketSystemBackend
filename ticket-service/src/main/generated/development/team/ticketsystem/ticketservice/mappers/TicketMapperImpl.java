package development.team.ticketsystem.ticketservice.mappers;

import development.team.ticketsystem.ticketservice.dto.tickets.CreateTicketRequest;
import development.team.ticketsystem.ticketservice.dto.tickets.TicketResponse;
import development.team.ticketsystem.ticketservice.dto.tickets.UpdateTicketRequest;
import development.team.ticketsystem.ticketservice.entity.TicketEntity;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2026-03-31T01:32:06+0300",
    comments = "version: 1.5.5.Final, compiler: javac, environment: Java 21.0.8 (Microsoft)"
)
@Component
public class TicketMapperImpl implements TicketMapper {

    @Override
    public TicketResponse toResponse(TicketEntity entity) {
        if ( entity == null ) {
            return null;
        }

        TicketResponse.TicketResponseBuilder ticketResponse = TicketResponse.builder();

        ticketResponse.id( entity.getId() );
        ticketResponse.subject( entity.getSubject() );
        ticketResponse.description( entity.getDescription() );
        ticketResponse.status( entity.getStatus() );
        ticketResponse.categoryId( entity.getCategoryId() );
        ticketResponse.createdBy( entity.getCreatedBy() );
        ticketResponse.assigneeId( entity.getAssigneeId() );
        ticketResponse.createdAt( entity.getCreatedAt() );
        ticketResponse.updatedAt( entity.getUpdatedAt() );

        return ticketResponse.build();
    }

    @Override
    public TicketEntity toEntity(CreateTicketRequest request) {
        if ( request == null ) {
            return null;
        }

        TicketEntity.TicketEntityBuilder ticketEntity = TicketEntity.builder();

        ticketEntity.subject( request.getSubject() );
        ticketEntity.description( request.getDescription() );
        ticketEntity.categoryId( request.getCategoryId() );

        return ticketEntity.build();
    }

    @Override
    public void updateEntityFromRequest(UpdateTicketRequest request, TicketEntity entity) {
        if ( request == null ) {
            return;
        }

        entity.setSubject( request.getSubject() );
        entity.setDescription( request.getDescription() );
    }
}
