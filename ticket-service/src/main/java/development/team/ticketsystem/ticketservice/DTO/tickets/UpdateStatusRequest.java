package development.team.ticketsystem.ticketservice.DTO.tickets;

import development.team.ticketsystem.ticketservice.TicketStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@AllArgsConstructor
@NoArgsConstructor
@Data
@ToString
public class UpdateStatusRequest {
    private TicketStatus status;
}