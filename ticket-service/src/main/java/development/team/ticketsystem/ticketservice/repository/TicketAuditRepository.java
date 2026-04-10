package development.team.ticketsystem.ticketservice.repository;

import development.team.ticketsystem.ticketservice.entity.TicketAuditEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface TicketAuditRepository extends JpaRepository<TicketAuditEntity, UUID> {

    List<TicketAuditEntity> findByTicketIdOrderByChangedAtDesc(UUID ticketId);
}