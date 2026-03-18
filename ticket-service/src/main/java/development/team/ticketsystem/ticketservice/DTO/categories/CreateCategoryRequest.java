package development.team.ticketsystem.ticketservice.DTO.categories;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Data
@ToString
public class CreateCategoryRequest {

    private UUID topicId;
    private String name;
    private String description;

}