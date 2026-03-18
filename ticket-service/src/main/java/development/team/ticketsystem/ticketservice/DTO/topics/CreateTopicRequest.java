package development.team.ticketsystem.ticketservice.DTO.topics;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@AllArgsConstructor
@NoArgsConstructor
@Data
@ToString
public class CreateTopicRequest {
    private String name;
    private String description;
}