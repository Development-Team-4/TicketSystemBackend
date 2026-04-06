package development.team.ticketsystem.ticketservice.dto.filter.filterFactory;

import development.team.ticketsystem.ticketservice.dto.filter.TicketFilter;
import development.team.ticketsystem.ticketservice.dto.filter.TicketFilterRequest;

import java.util.UUID;

public interface FilterBuilder {
    TicketFilter build(UUID userId, TicketFilterRequest request);
}
