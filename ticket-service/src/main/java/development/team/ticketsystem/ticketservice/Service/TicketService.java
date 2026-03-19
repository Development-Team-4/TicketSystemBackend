package development.team.ticketsystem.ticketservice.Service;

import development.team.ticketsystem.ticketservice.Entity.TicketEntity;
import development.team.ticketsystem.ticketservice.Repository.Specification.TicketSpecification;
import development.team.ticketsystem.ticketservice.Repository.TicketRepository;
import development.team.ticketsystem.ticketservice.TicketStatus;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Service
public class TicketService {

    private final TicketRepository repository;

    public TicketService(TicketRepository repository) {
        this.repository = repository;
    }

    public TicketEntity create(TicketEntity ticket) {
        ticket.setCreatedBy(UUID.fromString("3e3789e6-74ab-4101-95f2-f88876b0ed8c")); // Временно для тестов
        ticket.setCreatedAt(Instant.now());
        ticket.setUpdatedAt(Instant.now());
        return repository.save(ticket);
    }

    public List<TicketEntity> getAll(
            UUID categoryId,
            UUID assignedTo,
            UUID createdBy,
            String status,
            Instant createdAfter,
            Instant createdBefore
    ) {
        return repository.findAll(
                TicketSpecification.filter(
                        categoryId,
                        assignedTo,
                        createdBy,
                        status,
                        createdAfter,
                        createdBefore
                )
        );
    }

    public TicketEntity getById(UUID id) {
        return repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Ticket not found"));
    }

    public TicketEntity update(UUID id, TicketEntity updated) {
        TicketEntity existing = getById(id);

        existing.setSubject(updated.getSubject());
        existing.setDescription(updated.getDescription());
        existing.setUpdatedAt(Instant.now());

        return repository.save(existing);
    }

    public void delete(UUID id) {
        if (!repository.existsById(id)) {
            throw new EntityNotFoundException("Ticket not found");
        }
        repository.deleteById(id);
    }

    public TicketEntity updateStatus(UUID id, String status) {
        TicketEntity ticket = getById(id);
        ticket.setStatus(TicketStatus.valueOf(status));
        ticket.setUpdatedAt(Instant.now());
        return repository.save(ticket);
    }

    public TicketEntity assign(UUID id, UUID assigneeId) {
        TicketEntity ticket = getById(id);
        ticket.setAssigneeId(assigneeId);
        ticket.setUpdatedAt(Instant.now());
        return repository.save(ticket);
    }
}