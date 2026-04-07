package development.team.ticketsystem.ticketservice.service;

import development.team.ticketsystem.ticketservice.TicketStatus;
import development.team.ticketsystem.ticketservice.UserRole;
import development.team.ticketsystem.ticketservice.dto.filter.TicketFilter;
import development.team.ticketsystem.ticketservice.dto.filter.TicketFilterRequest;
import development.team.ticketsystem.ticketservice.dto.filter.filterStrategy.FilterBuilder;
import development.team.ticketsystem.ticketservice.dto.filter.filterStrategy.FilterResolver;
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
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.support.TransactionTemplate;

import java.time.Instant;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

@RequiredArgsConstructor
@Service
public class TicketService {

    private final TicketRepository ticketRepository;
    private final TicketCriteriaRepository ticketCriteriaRepository;
    private final CategoryStaffService categoryStaffService;
    private final NotificationSender notificationSender;
    private final TicketMapper ticketMapper;
    private final TransactionTemplate transactionTemplate;
    private final FilterResolver filterResolver;

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
        TicketEntity saved = ticketRepository.save(ticket);
        return ticketMapper.toResponse(saved);

    }

    public List<TicketResponse> getAll(
            UserRole role,
            UUID userId,
            TicketFilterRequest filterRequest
    ) throws AccessDeniedException {

        validateRoleAccessToFilters(
                role,
                userId,
                filterRequest
        );

        TicketFilter criteriaFilter = buildCriteriaFilter(
                role,
                userId,
                filterRequest
        );

        return ticketCriteriaRepository.findAllByFilters(criteriaFilter)
                .stream()
                .map(ticketMapper::toResponse)
                .toList();
    }

    private void validateRoleAccessToFilters(UserRole role, UUID userId, TicketFilterRequest filters) {
        if (role.equals(UserRole.SUPPORT)) {
            if (filters.getCreatedBy() != null) {
                throw new AccessDeniedException("Unable to filter by createdBy");
            }
            if (filters.getAssignedTo() != null && !filters.getAssignedTo().equals(userId)) {
                throw new AccessDeniedException("Staff can not view tickets assigned to others");
            }
        } else if (role.equals(UserRole.USER)) {
            if (filters.getAssignedTo() != null) {
                throw new AccessDeniedException("Users can not filter by assignedTo");
            }
        }
    }

    private TicketFilter buildCriteriaFilter(
            UserRole role,
            UUID userId,
            TicketFilterRequest request
    ) {
        FilterBuilder builder = filterResolver.resolve(role);
        return builder.build(userId, request);
    }

    public TicketResponse getById(UserRole role, UUID userId, UUID id) throws EntityNotFoundException {

        TicketEntity ticket = ticketRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Ticket not found"));

        if (!canViewTicket(role, userId, ticket)) {
            throw new AccessDeniedException("Access denied to this ticket");
        }

        return ticketMapper.toResponse(ticket);
    }

    public TicketResponse update(
            UserRole role,
            UUID userId,
            UUID id,
            UpdateTicketRequest request
    ) throws EntityNotFoundException, AccessDeniedException {

        TicketEntity updated = transactionTemplate.execute(status -> {

            TicketEntity existing = ticketRepository.findById(id)
                    .orElseThrow(() -> new EntityNotFoundException("Ticket not found"));

            checkNotClosed(existing);

            if (!canEditTicket(role, userId, existing)) {
                throw new AccessDeniedException("Cannot edit this ticket");
            }

            existing.setSubject(request.getSubject())
                    .setDescription(request.getDescription())
                    .setUpdatedAt(Instant.now());

            return ticketRepository.save(existing);
        });

        if (updated == null) {
            throw new RuntimeException("Updating ticket transaction failed");
        }

        return ticketMapper.toResponse(updated);
    }

    public void delete(UserRole role, UUID userId, UUID id) throws EntityNotFoundException, InvalidStateException {

        transactionTemplate.executeWithoutResult(status -> {

            TicketEntity ticket = ticketRepository.findById(id)
                    .orElseThrow(() -> new EntityNotFoundException("Ticket not found"));

            if (!ticket.getStatus().equals(TicketStatus.OPEN)) {
                throw new InvalidStateException("Only OPEN tickets can be deleted");
            }

            if (!canDeleteTicket(role, userId, ticket)) {
                throw new AccessDeniedException("Cannot delete this ticket");
            }

            ticketRepository.deleteById(id);
        });

    }

    public TicketResponse updateStatus(UUID id, UpdateStatusRequest request)
            throws EntityNotFoundException, InvalidStateException {

        TicketEntity updated = transactionTemplate.execute(status -> {

            TicketEntity ticket = ticketRepository.findById(id)
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

            return ticketRepository.save(ticket);

        });

        if (updated == null) {
            throw new RuntimeException("Updating ticket status transaction failed");
        }

        sendToNotificationMicroservice(
                updated.getCreatedBy(),
                updated.getId(),
                NotificationType.STATUS_CHANGE
        );

        return ticketMapper.toResponse(updated);
    }


    public TicketResponse assign(UUID id, AssignTicketRequest assigneeId)
            throws EntityNotFoundException, InvalidStateException {

        TicketEntity assigned = transactionTemplate.execute(status -> {

            TicketEntity ticket = ticketRepository.findById(id)
                    .orElseThrow(() -> new EntityNotFoundException("Ticket not found"));

            if (ticket.getStatus().equals(TicketStatus.CLOSED)) {
                throw new InvalidStateException("Cannot assign closed ticket");
            }

            ticket.setAssigneeId(assigneeId.getAssigneeId())
                    .setUpdatedAt(Instant.now());

            return ticketRepository.save(ticket);

        });

        if (assigned == null) {
            throw new RuntimeException("Assigning ticket transaction failed");
        }

        sendToNotificationMicroservice(
                assigned.getCreatedBy(),
                assigned.getId(),
                NotificationType.ASSIGNMENT
        );

        return ticketMapper.toResponse(assigned);
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

    private boolean canViewTicket(UserRole role, UUID userId, TicketEntity ticket) {

        if (role == UserRole.ADMIN) {
            return true;
        }

        if (role == UserRole.USER) {
            return ticket.getCreatedBy().equals(userId);
        }

        if (role == UserRole.SUPPORT) {
            boolean isAssignee = userId.equals(ticket.getAssigneeId());

            boolean inCategory = categoryStaffService.getByUser(userId)
                    .stream()
                    .map(CategoryStaffEntity::getCategoryId)
                    .anyMatch(catId -> catId.equals(ticket.getCategoryId()));

            return isAssignee || inCategory;
        }

        return false; // на будущее для новых ролей
    }

    private boolean canEditTicket(UserRole role, UUID userId, TicketEntity ticket) {

        if (role == UserRole.ADMIN) {
            return true;
        }

        if (role == UserRole.USER) {
            return ticket.getCreatedBy().equals(userId);
        }

        if (role == UserRole.SUPPORT) {
            return userId.equals(ticket.getAssigneeId());
        }

        return false;
    }

    private boolean canDeleteTicket(UserRole role, UUID userId, TicketEntity ticket) {

        if (role == UserRole.ADMIN) {
            return true;
        }

        if (role == UserRole.USER) {
            return ticket.getCreatedBy().equals(userId);
        }

        if (role == UserRole.SUPPORT) {
            boolean isAssignee = userId.equals(ticket.getAssigneeId());

            boolean inCategory = categoryStaffService.getByUser(userId)
                    .stream()
                    .map(CategoryStaffEntity::getCategoryId)
                    .anyMatch(catId -> catId.equals(ticket.getCategoryId()));

            return isAssignee || inCategory;
        }

        return false;
    }
}