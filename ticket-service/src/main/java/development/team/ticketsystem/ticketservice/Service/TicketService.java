package development.team.ticketsystem.ticketservice.Service;

import development.team.ticketsystem.ticketservice.DTO.tickets.*;
import development.team.ticketsystem.ticketservice.Entity.TicketEntity;
import development.team.ticketsystem.ticketservice.Repository.Specification.TicketSpecification;
import development.team.ticketsystem.ticketservice.Repository.TicketRepository;
import development.team.ticketsystem.ticketservice.TicketStatus;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.jspecify.annotations.NonNull;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

@RequiredArgsConstructor
@Service
public class TicketService {

    private final TicketRepository repository;

    public TicketResponse create(CreateTicketRequest request) {
        TicketEntity ticket = TicketEntity.builder()
                .subject(request.getSubject())
                .description(request.getDescription())
                .categoryId(request.getCategoryId())
                .status(TicketStatus.OPEN)
                .createdBy(UUID.fromString("3e3789e6-74ab-4101-95f2-f88876b0ed8c")) // временно
                .createdAt(Instant.now())
                .updatedAt(Instant.now())
                .build();
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

    @Transactional
    public TicketResponse update(UUID id, UpdateTicketRequest request) {
        TicketEntity existing = repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Ticket not found"));

        existing.setSubject(request.getSubject())
                .setDescription(request.getDescription())
                .setUpdatedAt(Instant.now());

        TicketEntity updated = repository.save(existing);
        return toResponse(updated);
    }

    @Transactional
    public void delete(UUID id) {
        if (!repository.existsById(id)) {
            throw new EntityNotFoundException("Ticket not found");
        }
        repository.deleteById(id);
    }

    @Transactional
    public TicketResponse updateStatus(UUID id, UpdateStatusRequest request) {
        TicketEntity ticket = repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Ticket not found"));
        ticket.setStatus(request.getStatus())
                .setUpdatedAt(Instant.now());
        TicketEntity updated = repository.save(ticket);
        return toResponse(updated);
    }

    @Transactional
    public TicketResponse assign(UUID id, AssignTicketRequest assigneeId) {
        TicketEntity ticket = repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Ticket not found"));

        ticket.setAssigneeId(assigneeId.getAssigneeId())
                .setUpdatedAt(Instant.now());
        TicketEntity assigned = repository.save(ticket);
        return toResponse(assigned);
    }

    // mapper - ИСПОЛЬЗОВАТЬ БИБЛИОТЕКУ mapstruct
    private TicketResponse toResponse(TicketEntity entity) {
        return TicketResponse.builder()
                .id(entity.getId())
                .subject(entity.getSubject())
                .description(entity.getDescription())
                .status(entity.getStatus())
                .categoryId(entity.getCategoryId())
                .createdBy(entity.getCreatedBy())
                .assigneeId(entity.getAssigneeId())
                .createdAt(entity.getCreatedAt())
                .updatedAt(entity.getUpdatedAt())
                .build();
    }
}