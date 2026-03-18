package development.team.ticketsystem.ticketservice.DTO.comments;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@AllArgsConstructor
@NoArgsConstructor
@Data
@ToString
public class CreateCommentRequest {
    private String content;
}