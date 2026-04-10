package development.team.ticketsystem.ticketservice.dto.audit;

import io.swagger.v3.oas.annotations.media.Schema;

import java.time.Instant;
import java.util.UUID;

@Schema(description = "История изменений тикета")
public class TicketAuditResponse {

    private String operation;

    private String oldStatus;
    private String newStatus;

    private UUID oldAssigneeId;
    private UUID newAssigneeId;

    private Instant changedAt;
}