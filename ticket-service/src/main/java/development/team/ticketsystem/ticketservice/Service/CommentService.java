package development.team.ticketsystem.ticketservice.Service;

import development.team.ticketsystem.ticketservice.DTO.comments.CommentResponse;
import development.team.ticketsystem.ticketservice.DTO.comments.CreateCommentRequest;
import development.team.ticketsystem.ticketservice.Entity.CommentEntity;
import development.team.ticketsystem.ticketservice.Repository.CommentRepository;
import development.team.ticketsystem.ticketservice.Repository.TicketRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;
import java.util.UUID;


@Service
public class CommentService {

    private final CommentRepository repository;
    private final TicketRepository ticketRepository;

    public CommentService(CommentRepository repository,
                          TicketRepository ticketRepository) {
        this.repository = repository;
        this.ticketRepository = ticketRepository;
    }

    public List<CommentResponse> getByTicket(UUID ticketId) {
        if (!ticketRepository.existsById(ticketId)) {
            throw new EntityNotFoundException("Ticket not found");
        }
        return repository.findByTicketId(ticketId)
                .stream()
                .map(this::toResponse)
                .toList();
    }

    public CommentResponse create(UUID ticketId, UUID authorId, CreateCommentRequest request) {

        if (!ticketRepository.existsById(ticketId)) {
            throw new EntityNotFoundException("Ticket not found");
        }

        CommentEntity comment = new CommentEntity();
        comment.setTicketId(ticketId);
        comment.setAuthorId(authorId);
        comment.setContent(request.getContent());
        comment.setCreatedAt(Instant.now());

        CommentEntity saved = repository.save(comment);

        return toResponse(saved);
    }

    // mapper - ИСПОЛЬЗОВАТЬ БИБЛИОТЕКУ mapstruct
    private CommentResponse toResponse(CommentEntity entity) {
        CommentResponse response = new CommentResponse();
        response.setId(entity.getId());
        response.setTicketId(entity.getTicketId());
        response.setContent(entity.getContent());
        response.setAuthorId(entity.getAuthorId());
        response.setCreatedAt(entity.getCreatedAt());
        return response;
    }
}