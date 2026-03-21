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

        CommentEntity comment = CommentEntity.builder()
                .ticketId(ticketId)
                .authorId(authorId)
                .content(request.getContent())
                .createdAt(Instant.now())
                .build();

        CommentEntity saved = repository.save(comment);

        return toResponse(saved);
    }

    // mapper - ИСПОЛЬЗОВАТЬ БИБЛИОТЕКУ mapstruct
    private CommentResponse toResponse(CommentEntity entity) {
        return CommentResponse.builder()
                .id(entity.getId())
                .ticketId(entity.getTicketId())
                .content(entity.getContent())
                .authorId(entity.getAuthorId())
                .createdAt(entity.getCreatedAt())
                .build();
    }
}