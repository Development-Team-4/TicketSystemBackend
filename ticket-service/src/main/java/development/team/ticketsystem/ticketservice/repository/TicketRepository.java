package development.team.ticketsystem.ticketservice.repository;

import development.team.ticketsystem.ticketservice.entity.TicketEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.UUID;

public interface TicketRepository extends
        JpaRepository<TicketEntity, UUID>,
        JpaSpecificationExecutor<TicketEntity> {
}