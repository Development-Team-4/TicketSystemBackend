package development.team.ticketsystem.ticketservice.Service;

import development.team.ticketsystem.ticketservice.DTO.tickets.*;
import development.team.ticketsystem.ticketservice.Entity.TicketEntity;
import development.team.ticketsystem.ticketservice.Repository.Specification.TicketSpecification;
import development.team.ticketsystem.ticketservice.Repository.TicketRepository;
import development.team.ticketsystem.ticketservice.TicketStatus;
import jakarta.persistence.EntityNotFoundException;
import org.jspecify.annotations.NonNull;
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

    public TicketResponse create(CreateTicketRequest request) {
        TicketEntity ticket = new TicketEntity();
        ticket.setSubject(request.getSubject());
        ticket.setDescription(request.getDescription());
        ticket.setCategoryId(request.getCategoryId());
        ticket.setStatus(TicketStatus.valueOf(TicketStatus.OPEN.name()));

        ticket.setCreatedBy(UUID.fromString("3e3789e6-74ab-4101-95f2-f88876b0ed8c")); // Временно для тестов
        ticket.setCreatedAt(Instant.now());
        ticket.setUpdatedAt(Instant.now());
        TicketEntity saved = repository.save(ticket);
        return toResponse(saved);

    }

    public List<TicketResponse> getAll(
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
                ).stream()
                .map(this::toResponse)
                .toList();
    }

    public TicketResponse getById(UUID id) {
        return toResponse(repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Ticket not found")));
    }

    public TicketResponse update(UUID id, UpdateTicketRequest request) {
        TicketEntity existing = repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Ticket not found"));

        existing.setSubject(request.getSubject());
        existing.setDescription(request.getDescription());
        existing.setUpdatedAt(Instant.now());

        TicketEntity updated = repository.save(existing);
        return toResponse(updated);
    }

    public void delete(UUID id) {
        if (!repository.existsById(id)) {
            throw new EntityNotFoundException("Ticket not found");
        }
        repository.deleteById(id);
    }

    public TicketResponse updateStatus(UUID id, UpdateStatusRequest request) {
        TicketEntity ticket = repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Ticket not found"));
        ticket.setStatus(request.getStatus());
        ticket.setUpdatedAt(Instant.now());
        TicketEntity updated = repository.save(ticket);
        return toResponse(updated);
    }

    public TicketResponse assign(UUID id, AssignTicketRequest assigneeId) {
        TicketEntity ticket = repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Ticket not found"));
        ticket.setAssigneeId(assigneeId.getAssigneeId());
        ticket.setUpdatedAt(Instant.now());
        TicketEntity assigned = repository.save(ticket);
        return toResponse(assigned);
    }

    // mapper - ИСПОЛЬЗОВАТЬ БИБЛИОТЕКУ mapstruct
    private TicketResponse toResponse(@NonNull TicketEntity entity) {
        TicketResponse response = new TicketResponse();

        response.setId(entity.getId());
        response.setSubject(entity.getSubject());
        response.setDescription(entity.getDescription());
        response.setStatus(TicketStatus.valueOf(String.valueOf(entity.getStatus())));

        response.setCategoryId(entity.getCategoryId());
        response.setCreatedBy(entity.getCreatedBy());
        response.setAssigneeId(entity.getAssigneeId());

        response.setCreatedAt(entity.getCreatedAt());
        response.setUpdatedAt(entity.getUpdatedAt());

        return response;
    }
}