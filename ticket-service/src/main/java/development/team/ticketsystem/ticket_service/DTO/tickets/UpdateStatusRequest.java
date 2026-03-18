package development.team.ticketsystem.ticket_service.DTO.tickets;

import development.team.ticketsystem.ticket_service.TicketStatus;
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