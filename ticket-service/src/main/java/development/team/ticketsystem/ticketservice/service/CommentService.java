package development.team.ticketsystem.ticketservice.service;

import development.team.ticketsystem.ticketservice.TicketStatus;
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
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.support.TransactionTemplate;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

@RequiredArgsConstructor
@Service
public class CommentService {

    private final CommentRepository commentRepository;
    private final TicketRepository ticketRepository;
    private final NotificationSender notificationSender;
    private final CommentMapper commentMapper;

    private final TransactionTemplate transactionTemplate;

    public List<CommentResponse> getByTicket(UUID ticketId) {
        return commentRepository.findByTicketId(ticketId)
                .stream()
                .map(commentMapper::toResponse)
                .toList();
    }


    public CommentResponse create(UUID ticketId, UUID authorId, CreateCommentRequest request)
            throws EntityNotFoundException, InvalidStateException {

        CreateCommentTransactionResult result = transactionTemplate.execute(status -> {

            TicketEntity ticket = ticketRepository.findById(ticketId)
                    .orElseThrow(() -> new EntityNotFoundException("Ticket not found"));

            if (ticket.getStatus().equals(TicketStatus.CLOSED)) {
                throw new InvalidStateException("Cannot comment CLOSED ticket");
            }

            CommentEntity comment = CommentEntity.builder()
                    .ticketId(ticketId)
                    .authorId(authorId)
                    .content(request.getContent())
                    .createdAt(Instant.now())
                    .build();

            CommentEntity saved = commentRepository.save(comment);

            return new CreateCommentTransactionResult(ticket, saved);
        });

        if (result == null) {
            throw new RuntimeException("Create comment transaction failed");
        }

        notificationSender.sendToNotificationMicroservice(
                result.ticket().getCreatedBy(),
                new NotificationCreationDto(
                        result.ticket().getCreatedBy(),
                        result.ticket().getId(),
                        NotificationType.COMMENT
                )
        );

        return commentMapper.toResponse(result.comment());
    }

    private record CreateCommentTransactionResult(
            TicketEntity ticket,
            CommentEntity comment
    ) {
    }

}