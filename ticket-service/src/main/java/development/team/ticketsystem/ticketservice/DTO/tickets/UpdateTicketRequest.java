package development.team.ticketsystem.ticket_service.DTO.tickets;

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