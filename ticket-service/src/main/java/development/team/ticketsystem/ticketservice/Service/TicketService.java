package development.team.ticketsystem.ticketservice.Service;

import development.team.ticketsystem.ticketservice.DTO.tickets.*;
import development.team.ticketsystem.ticketservice.Entity.TicketEntity;
import development.team.ticketsystem.ticketservice.Exceptions.InvalidStateException;
import development.team.ticketsystem.ticketservice.Repository.Specification.TicketSpecification;
import development.team.ticketsystem.ticketservice.Repository.TicketRepository;
import development.team.ticketsystem.ticketservice.TicketStatus;
import development.team.ticketsystem.ticketservice.UserRole;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import development.team.ticketsystem.ticketservice.Exceptions.AccessDeniedException;
import development.team.ticketsystem.ticketservice.TicketStatus;

import java.time.Instant;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

@RequiredArgsConstructor
@Service
public class TicketService {

    private final TicketRepository repository;
    private final CategoryStaffService categoryStaffService;

    private static final Map<TicketStatus, Set<TicketStatus>> ALLOWED_TRANSITIONS = Map.of(
            TicketStatus.OPEN, Set.of(TicketStatus.ASSIGNED, TicketStatus.CLOSED),
            TicketStatus.ASSIGNED, Set.of(TicketStatus.IN_PROGRESS, TicketStatus.CLOSED),
            TicketStatus.IN_PROGRESS, Set.of(TicketStatus.RESOLVED, TicketStatus.ASSIGNED),
            TicketStatus.RESOLVED, Set.of(TicketStatus.CLOSED, TicketStatus.ASSIGNED),
            TicketStatus.CLOSED, Set.of()
    );

    public TicketResponse create(UUID authorId, CreateTicketRequest request) {
        TicketEntity ticket = TicketEntity.builder()
                .subject(request.getSubject())
                .description(request.getDescription())
                .categoryId(request.getCategoryId())
                .status(TicketStatus.OPEN)
                .createdBy(authorId)
                .createdAt(Instant.now())
                .updatedAt(Instant.now())
                .build();
        TicketEntity saved = repository.save(ticket);
        return toResponse(saved);

    }

    public List<TicketResponse> getAll(
            UserRole role,
            UUID userId,
            UUID categoryId,
            UUID assignedTo,
            UUID createdBy,
            String status,
            Instant createdAfter,
            Instant createdBefore
    ) {

        if (role.equals(UserRole.SUPPORT)) {
            if (categoryId == null) {
                categoryId = categoryStaffService.getByUser(userId).getFirst().getCategoryId();
            }
            if (assignedTo == null) {
                assignedTo = userId;
            } else if (!assignedTo.equals(userId)) {
                throw new AccessDeniedException("Staff can not view tickets assigned to others");
            }

            if (createdBy != null) {
                throw new AccessDeniedException("Unable to filter by createdBy");
            }
        } else if (role.equals(UserRole.USER)) {
            createdBy = userId;

            if (assignedTo != null) {
                throw new AccessDeniedException("Users can not filter by assignedTo");
            }
        }


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
    public TicketResponse update(
            UserRole role,
            UUID userId,
            UUID id,
            UpdateTicketRequest request) {
        TicketEntity existing = repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Ticket not found"));
        checkNotClosed(existing);

        if (role == UserRole.USER && !existing.getCreatedBy().equals(userId)) {
            throw new AccessDeniedException("Cannot edit other user's ticket");
        }

        existing.setSubject(request.getSubject())
                .setDescription(request.getDescription())
                .setUpdatedAt(Instant.now());

        TicketEntity updated = repository.save(existing);
        return toResponse(updated);
    }

    @Transactional
    public void delete(UserRole role, UUID userId, UUID id) {
        TicketEntity ticket = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Ticket not found"));

        if (ticket.getStatus() != TicketStatus.OPEN) {
            throw new InvalidStateException("Only OPEN tickets can be deleted");
        }
        if (role == UserRole.USER && !ticket.getCreatedBy().equals(userId)) {
            throw new InvalidStateException("You can not delete ticket of other user");
        }

        repository.deleteById(id);
    }

    @Transactional
    public TicketResponse updateStatus(UUID id, UpdateStatusRequest request) {
        TicketEntity ticket = repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Ticket not found"));
        checkNotClosed(ticket);

        TicketStatus current = ticket.getStatus();

        if (!ALLOWED_TRANSITIONS.get(current).contains(request.getStatus())) {
            throw new InvalidStateException(
                    "Invalid status transition: " + current + " -> " + request.getStatus()
            );
        }

        ticket.setStatus(request.getStatus())
                .setUpdatedAt(Instant.now());
        TicketEntity updated = repository.save(ticket);
        return toResponse(updated);
    }

    @Transactional
    public TicketResponse assign(UUID id, AssignTicketRequest assigneeId) {
        TicketEntity ticket = repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Ticket not found"));
        checkNotClosed(ticket);

        if (ticket.getStatus().equals(TicketStatus.CLOSED)) {
            throw new InvalidStateException("Cannot assign closed ticket");
        }

        ticket.setAssigneeId(assigneeId.getAssigneeId())
                .setUpdatedAt(Instant.now());
        TicketEntity assigned = repository.save(ticket);
        return toResponse(assigned);
    }

    private void checkNotClosed(TicketEntity ticket) {
        if (ticket.getStatus() == TicketStatus.CLOSED) {
            throw new InvalidStateException("Ticket is CLOSED");
        }
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