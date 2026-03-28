package development.team.ticketsystem.ticketservice.service;

import development.team.ticketsystem.ticketservice.dto.comments.CommentResponse;
import development.team.ticketsystem.ticketservice.dto.comments.CreateCommentRequest;
import development.team.ticketsystem.ticketservice.entity.CommentEntity;
import development.team.ticketsystem.ticketservice.entity.TicketEntity;
import development.team.ticketsystem.ticketservice.exceptions.InvalidStateException;
import development.team.ticketsystem.ticketservice.forNotificationMicroservice.NotificationCreationDto;
import development.team.ticketsystem.ticketservice.forNotificationMicroservice.NotificationType;
import development.team.ticketsystem.ticketservice.mappers.CommentMapper;
import development.team.ticketsystem.ticketservice.repository.CommentRepository;
import development.team.ticketsystem.ticketservice.repository.TicketRepository;
import development.team.ticketsystem.ticketservice.TicketStatus;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

@RequiredArgsConstructor
@Service
public class CommentService {

    private final CommentRepository repository;
    private final TicketRepository ticketRepository;
    private final NotificationSender notificationSender;
    private final CommentMapper mapper;

    public List<CommentResponse> getByTicket(UUID ticketId) {
        return repository.findByTicketId(ticketId)
                .stream()
                .map(mapper::toResponse)
                .toList();
    }


    public CommentResponse create(UUID ticketId, UUID authorId, CreateCommentRequest request)
            throws EntityNotFoundException, InvalidStateException {

        TicketEntity ticket = ticketRepository.findById(ticketId)
                .orElseThrow(() -> new EntityNotFoundException("Ticket not found"));

        if (ticket.getStatus().equals(TicketStatus.CLOSED)) {
            throw new InvalidStateException("Cannot comment CLOSED ticket");
        }

        CommentEntity saved = createCommentWithTransaction(ticketId, authorId, request);

        notificationSender.sendToNotificationMicroservice(
                ticket.getCreatedBy(),
                new NotificationCreationDto(
                        ticket.getCreatedBy(),
                        ticket.getId(),
                        NotificationType.COMMENT
                )
        );

        return mapper.toResponse(saved);
    }

    @Transactional
    private CommentEntity createCommentWithTransaction(UUID ticketId, UUID authorId, CreateCommentRequest request)
            throws EntityNotFoundException, InvalidStateException {

        CommentEntity comment = CommentEntity.builder()
                .ticketId(ticketId)
                .authorId(authorId)
                .content(request.getContent())
                .createdAt(Instant.now())
                .build();

        return repository.save(comment);
    }

}