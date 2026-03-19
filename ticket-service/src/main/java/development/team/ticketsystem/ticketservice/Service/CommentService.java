package development.team.ticketsystem.ticketservice.Service;

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

    public List<CommentEntity> getByTicket(UUID ticketId) {
        if (!ticketRepository.existsById(ticketId)) {
            throw new EntityNotFoundException("Ticket not found");
        }
        return repository.findByTicketId(ticketId);
    }

    public CommentEntity create(UUID ticketId, UUID authorId, String content) {

        if (!ticketRepository.existsById(ticketId)) {
            throw new EntityNotFoundException("Ticket not found");
        }

        CommentEntity comment = new CommentEntity();
        comment.setTicketId(ticketId);
        comment.setAuthorId(authorId);
        comment.setContent(content);
        comment.setCreatedAt(Instant.now());

        return repository.save(comment);
    }
}