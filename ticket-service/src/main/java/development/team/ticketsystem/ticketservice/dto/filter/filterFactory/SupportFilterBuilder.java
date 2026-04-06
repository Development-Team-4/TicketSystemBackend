package development.team.ticketsystem.ticketservice.dto.filter.filterFactory;

import development.team.ticketsystem.ticketservice.UserRole;
import development.team.ticketsystem.ticketservice.dto.filter.TicketFilter;
import development.team.ticketsystem.ticketservice.dto.filter.TicketFilterRequest;
import development.team.ticketsystem.ticketservice.entity.CategoryStaffEntity;
import development.team.ticketsystem.ticketservice.service.CategoryStaffService;

import java.util.List;
import java.util.UUID;

public record SupportFilterBuilder(CategoryStaffService categoryStaffService) implements FilterBuilder {
    @Override
    public TicketFilter build(UUID userId, TicketFilterRequest request) {
        List<UUID> categories = categoryStaffService.getByUser(userId)
                .stream()
                .map(CategoryStaffEntity::getCategoryId)
                .toList();

        return TicketFilter.builder()
                .role(UserRole.SUPPORT)
                .userId(userId)
                .categoryIds(request.getCategoryId() == null ? categories : List.of(request.getCategoryId()))
                .assignedTo(userId)
                .status(String.valueOf(request.getStatus()))
                .createdAfter(request.getCreatedAfter())
                .createdBefore(request.getCreatedBefore())
                .build();
    }
}
