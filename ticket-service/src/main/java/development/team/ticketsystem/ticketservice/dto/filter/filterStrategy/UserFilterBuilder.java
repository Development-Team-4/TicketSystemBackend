package development.team.ticketsystem.ticketservice.dto.filter.filterStrategy;

import development.team.ticketsystem.ticketservice.UserRole;
import development.team.ticketsystem.ticketservice.dto.filter.TicketFilter;
import development.team.ticketsystem.ticketservice.dto.filter.TicketFilterRequest;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public record UserFilterBuilder() implements FilterBuilder {
    @Override
    public TicketFilter build(UUID userId, TicketFilterRequest request) {
        return TicketFilter.builder()
                .role(UserRole.USER)
                .userId(userId)
                .createdBy(userId)
                .status(request.getStatus() != null ? request.getStatus().name() : null)
                .createdAfter(request.getCreatedAfter())
                .createdBefore(request.getCreatedBefore())
                .build();
    }

    @Override
    public boolean supports(UserRole role) {
        return role == UserRole.USER;
    }
}
