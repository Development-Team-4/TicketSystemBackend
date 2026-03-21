package development.team.ticketsystem.ticketservice.Controllers;

import development.team.ticketsystem.ticketservice.DTO.comments.CommentResponse;
import development.team.ticketsystem.ticketservice.DTO.comments.CreateCommentRequest;
import development.team.ticketsystem.ticketservice.Service.CommentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RequiredArgsConstructor
@RestController
@RequestMapping("/tickets/{ticketId}/comments")
@Tag(name = "Comments", description = "Комментарии к тикетам")
public class CommentController {

    private final CommentService service;


    @Operation(summary = "Получить все комментарии тикета")
    @GetMapping
    public List<CommentResponse> getComments(
            @Parameter(description = "ID тикета", example = "550e8400-e29b-41d4-a716-446655440000")
            @PathVariable UUID ticketId
    ) {
        return service.getByTicket(ticketId);
    }

    @Operation(summary = "Добавить комментарий к тикету")
    @PostMapping
    public CommentResponse addComment(
            @Parameter(description = "ID тикета", example = "550e8400-e29b-41d4-a716-446655440000")
            @PathVariable UUID ticketId,

            @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "Данные для создания комментария")
            @RequestBody CreateCommentRequest request
    ) {
        // временно authorId = 8f1e8e6e-3497-4690-bfc2-c7292e7438f1 (потом из JWT)
        return service.create(
                ticketId,
                UUID.fromString("8f1e8e6e-3497-4690-bfc2-c7292e7438f1"),
                request
        );
    }

}