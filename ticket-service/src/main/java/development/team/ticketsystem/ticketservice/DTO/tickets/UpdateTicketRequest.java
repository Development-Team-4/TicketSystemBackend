package development.team.ticketsystem.ticketservice.DTO.tickets;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@AllArgsConstructor
@NoArgsConstructor
@Data
@ToString
public class UpdateTicketRequest {
    private String subject;
    private String description;
}