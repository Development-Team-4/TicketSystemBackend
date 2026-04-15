package development.team.ticketsystem.ticketservice.dto.filter;

import development.team.ticketsystem.ticketservice.UserRole;
import lombok.Builder;
import lombok.Data;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Data
@Builder
public class TicketFilter {

    private UserRole role;
    private UUID userId;

    private UUID categoryId;
    private List<UUID> categoryIds;

    private UUID assignedTo;
    private UUID createdBy;

    private String status;
    private Instant createdAfter;
    private Instant createdBefore;
}