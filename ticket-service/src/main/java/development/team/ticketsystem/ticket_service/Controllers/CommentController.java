package development.team.ticketsystem.ticket_service.Controllers;

import development.team.ticketsystem.ticket_service.DTO.comments.CommentResponse;
import development.team.ticketsystem.ticket_service.DTO.comments.CreateCommentRequest;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/tickets/{ticketId}/comments")
public class CommentController {

    @GetMapping
    public List<CommentResponse> getComments(@PathVariable UUID ticketId) {
        return List.of();
    }

    @PostMapping
    public CommentResponse addComment(
            @PathVariable UUID ticketId,
            @RequestBody CreateCommentRequest request
    ) {
        return new CommentResponse();
    }

}