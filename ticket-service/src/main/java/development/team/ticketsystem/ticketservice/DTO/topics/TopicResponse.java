package development.team.ticketsystem.ticket_service.DTO.topics;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Data
@ToString
public class TopicResponse {
    private UUID id;
    private String name;
    private String description;
}