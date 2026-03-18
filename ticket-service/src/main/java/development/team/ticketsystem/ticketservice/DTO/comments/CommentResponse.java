package development.team.ticketsystem.ticket_service.DTO.comments;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.time.Instant;
import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Data
@ToString
public class CommentResponse {

    private UUID id;
    private UUID ticketId;
    private UUID authorId;

    private String content;

    private Instant createdAt;
}