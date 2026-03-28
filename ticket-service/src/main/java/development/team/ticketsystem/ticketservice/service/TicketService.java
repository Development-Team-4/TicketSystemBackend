package development.team.ticketsystem.ticketservice.service;

import development.team.ticketsystem.ticketservice.TicketStatus;
import development.team.ticketsystem.ticketservice.UserRole;
import development.team.ticketsystem.ticketservice.dto.filter.TicketFilter;
import development.team.ticketsystem.ticketservice.dto.tickets.*;
import development.team.ticketsystem.ticketservice.entity.CategoryStaffEntity;
import development.team.ticketsystem.ticketservice.entity.TicketEntity;
import development.team.ticketsystem.ticketservice.exceptions.AccessDeniedException;
import development.team.ticketsystem.ticketservice.exceptions.InvalidStateException;
import development.team.ticketsystem.ticketservice.exceptions.NotificationServiceException;
import development.team.ticketsystem.ticketservice.forNotificationMicroservice.NotificationCreationDto;
import development.team.ticketsystem.ticketservice.forNotificationMicroservice.NotificationType;
import development.team.ticketsystem.ticketservice.mappers.TicketMapper;
import development.team.ticketsystem.ticketservice.repository.TicketRepository;
import development.team.ticketsystem.ticketservice.repository.criteria.TicketCriteriaRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

@RequiredArgsConstructor
@Service
public class TicketService {

    private final TicketRepository repository;
    private final TicketCriteriaRepository ticketCriteriaRepository;
    private final CategoryStaffService categoryStaffService;
    private final NotificationSender notificationSender;
    private final TicketMapper mapper;

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
        return mapper.toResponse(saved);

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
    ) throws AccessDeniedException {

        validateRoleAccessToFilters(
                role,
                userId,
                assignedTo,
                createdBy
        );

        TicketFilter filter = buildFilter(
                role,
                userId,
                categoryId,
                assignedTo,
                createdBy,
                status,
                createdAfter,
                createdBefore
        );

        return ticketCriteriaRepository.findAllByFilters(filter)
                .stream()
                .map(mapper::toResponse)
                .toList();
    }

    private void validateRoleAccessToFilters(UserRole role, UUID userId, UUID assignedTo, UUID createdBy) {
        if (role.equals(UserRole.SUPPORT)) {
            if (createdBy != null) {
                throw new AccessDeniedException("Unable to filter by createdBy");
            }
            if (assignedTo != null && !assignedTo.equals(userId)) {
                throw new AccessDeniedException("Staff can not view tickets assigned to others");
            }
        } else if (role.equals(UserRole.USER)) {
            if (assignedTo != null) {
                throw new AccessDeniedException("Users can not filter by assignedTo");
            }
        }
    }

    private TicketFilter buildFilter(
            UserRole role,
            UUID userId,
            UUID categoryId,
            UUID assignedTo,
            UUID createdBy,
            String status,
            Instant createdAfter,
            Instant createdBefore
    ) {

        if (role == UserRole.SUPPORT) {

            List<UUID> categories = categoryStaffService.getByUser(userId)
                    .stream()
                    .map(CategoryStaffEntity::getCategoryId)
                    .toList();

            return TicketFilter.builder()
                    .role(role)
                    .userId(userId)
                    .categoryIds(categoryId == null ? categories : List.of(categoryId))
                    .assignedTo(userId)
                    .status(status)
                    .createdAfter(createdAfter)
                    .createdBefore(createdBefore)
                    .build();
        }

        if (role == UserRole.USER) {
            return TicketFilter.builder()
                    .role(role)
                    .userId(userId)
                    .createdBy(userId)
                    .status(status)
                    .createdAfter(createdAfter)
                    .createdBefore(createdBefore)
                    .build();
        }

        return TicketFilter.builder()
                .role(role)
                .userId(userId)
                .categoryId(categoryId)
                .assignedTo(assignedTo)
                .createdBy(createdBy)
                .status(status)
                .createdAfter(createdAfter)
                .createdBefore(createdBefore)
                .build();
    }

    public TicketResponse getById(UUID id) throws EntityNotFoundException {
        return mapper.toResponse(repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Ticket not found")));
    }

    @Transactional
    public TicketResponse update(
            UserRole role,
            UUID userId,
            UUID id,
            UpdateTicketRequest request
    ) throws EntityNotFoundException, AccessDeniedException {
        TicketEntity existing = repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Ticket not found"));
        checkNotClosed(existing);

        if (!role.equals(UserRole.ADMIN) && !existing.getCreatedBy().equals(userId)) {
            throw new AccessDeniedException("Cannot edit other user's ticket");
        }

        existing.setSubject(request.getSubject())
                .setDescription(request.getDescription())
                .setUpdatedAt(Instant.now());

        TicketEntity updated = repository.save(existing);
        return mapper.toResponse(updated);
    }

    @Transactional
    public void delete(UserRole role, UUID userId, UUID id) throws EntityNotFoundException, InvalidStateException {
        TicketEntity ticket = repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Ticket not found"));

        if (ticket.getStatus().equals(TicketStatus.OPEN)) {
            throw new InvalidStateException("Only OPEN tickets can be deleted");
        }
        if (role.equals(UserRole.USER) && !ticket.getCreatedBy().equals(userId)) {
            throw new InvalidStateException("You can not delete ticket of other user");
        }

        repository.deleteById(id);
    }

    public TicketResponse updateStatus(UUID id, UpdateStatusRequest request)
            throws EntityNotFoundException, InvalidStateException {

        TicketEntity updated = updateStatusWithTransaction(id, request);

        sendToNotificationMicroservice(
                updated.getCreatedBy(),
                updated.getId(),
                NotificationType.STATUS_CHANGE
        );

        return mapper.toResponse(updated);
    }


    public TicketResponse assign(UUID id, AssignTicketRequest assigneeId)
            throws EntityNotFoundException, InvalidStateException {

        TicketEntity assigned = assignTicketWithTransaction(id, assigneeId);

        sendToNotificationMicroservice(
                assigned.getCreatedBy(),
                assigned.getId(),
                NotificationType.ASSIGNMENT
        );

        return mapper.toResponse(assigned);
    }

    @Transactional
    private TicketEntity assignTicketWithTransaction(UUID id, AssignTicketRequest assigneeId)
            throws EntityNotFoundException, InvalidStateException {

        TicketEntity ticket = repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Ticket not found"));

        if (ticket.getStatus().equals(TicketStatus.CLOSED)) {
            throw new InvalidStateException("Cannot assign closed ticket");
        }

        ticket.setAssigneeId(assigneeId.getAssigneeId())
                .setUpdatedAt(Instant.now());

        return repository.save(ticket);
    }

    @Transactional
    private TicketEntity updateStatusWithTransaction(UUID id, UpdateStatusRequest request)
            throws EntityNotFoundException, InvalidStateException {

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

        return repository.save(ticket);
    }

    private void sendToNotificationMicroservice(
            UUID toUserId,
            UUID ticketId,
            NotificationType type
    ) throws NotificationServiceException {
        notificationSender.sendToNotificationMicroservice(
                toUserId,
                new NotificationCreationDto(
                        toUserId,
                        ticketId,
                        type
                ));
    }

    private void checkNotClosed(TicketEntity ticket) throws InvalidStateException {
        if (ticket.getStatus() == TicketStatus.CLOSED) {
            throw new InvalidStateException("Ticket is CLOSED");
        }
    }

}