package development.team.ticketsystem.ticketservice.Controllers;

import development.team.ticketsystem.ticketservice.DTO.comments.CommentResponse;
import development.team.ticketsystem.ticketservice.DTO.comments.CreateCommentRequest;
import development.team.ticketsystem.ticketservice.Entity.CommentEntity;
import development.team.ticketsystem.ticketservice.Service.CommentService;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/tickets/{ticketId}/comments")
public class CommentController {

    private final CommentService service;

    public CommentController(CommentService service) {
        this.service = service;
    }

    @GetMapping
    public List<CommentResponse> getComments(@PathVariable UUID ticketId) {
        return service.getByTicket(ticketId)
                .stream()
                .map(this::toResponse)
                .toList();
    }

    @PostMapping
    public CommentResponse addComment(
            @PathVariable UUID ticketId,
            @RequestBody CreateCommentRequest request
    ) {
        // временно authorId = 8f1e8e6e-3497-4690-bfc2-c7292e7438f1 (потом из JWT)
        CommentEntity comment = service.create(ticketId,
                UUID.fromString("8f1e8e6e-3497-4690-bfc2-c7292e7438f1"),
                request.getContent());
        return toResponse(comment);
    }

    // mapper
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