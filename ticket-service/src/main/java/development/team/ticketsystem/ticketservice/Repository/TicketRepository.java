package development.team.ticketsystem.ticketservice.Repository;

import development.team.ticketsystem.ticketservice.Entity.TicketEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.UUID;

public interface TicketRepository extends
        JpaRepository<TicketEntity, UUID>,
        JpaSpecificationExecutor<TicketEntity> {
}