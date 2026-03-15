package development.team.ticketsystem.ticket_service.DTO.tickets;

import development.team.ticketsystem.ticket_service.DTO.TicketStatus;
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
public class TicketResponse {

    private UUID id;
    private String subject;
    private String description;
    private TicketStatus status;

    private UUID categoryId;
    private UUID createdBy;
    private UUID assigneeId;

    private Instant createdAt;
    private Instant updatedAt;
}