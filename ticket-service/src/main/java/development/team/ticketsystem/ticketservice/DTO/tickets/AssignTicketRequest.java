package development.team.ticketsystem.ticket_service.DTO.tickets;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Data
@ToString
public class AssignTicketRequest {
    private UUID assigneeId;
}