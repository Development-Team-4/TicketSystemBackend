package development.team.ticketsystem.ticketservice.Service;

import development.team.ticketsystem.ticketservice.DTO.comments.CommentResponse;
import development.team.ticketsystem.ticketservice.DTO.comments.CreateCommentRequest;
import development.team.ticketsystem.ticketservice.Entity.CommentEntity;
import development.team.ticketsystem.ticketservice.Entity.TicketEntity;
import development.team.ticketsystem.ticketservice.Exceptions.InvalidStateException;
import development.team.ticketsystem.ticketservice.ForNotificationMicroservice.dto.NotificationCreationDto;
import development.team.ticketsystem.ticketservice.ForNotificationMicroservice.dto.NotificationType;
import development.team.ticketsystem.ticketservice.Mappers.CommentMapper;
import development.team.ticketsystem.ticketservice.Repository.CommentRepository;
import development.team.ticketsystem.ticketservice.Repository.TicketRepository;
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

    @Transactional
    public CommentResponse create(UUID ticketId, UUID authorId, CreateCommentRequest request) {
        TicketEntity ticket = ticketRepository.findById(ticketId)
                .orElseThrow(() -> new RuntimeException("Ticket not found"));

        if (ticket.getStatus() == TicketStatus.CLOSED) {
            throw new InvalidStateException("Cannot comment CLOSED ticket");
        }


        CommentEntity comment = CommentEntity.builder()
                .ticketId(ticketId)
                .authorId(authorId)
                .content(request.getContent())
                .createdAt(Instant.now())
                .build();

        CommentEntity saved = repository.save(comment);
        // отложено из-за проблем сервиса нотификаций
//        notificationSender.sendToNotificationMicroservice (
//                ticket.getCreatedBy(),
//        new NotificationCreationDto(
//                ticket.getCreatedBy(),
//                ticket.getId(),
//                NotificationType.STATUS_CHANGE
//        ));


        return mapper.toResponse(saved);
    }

}