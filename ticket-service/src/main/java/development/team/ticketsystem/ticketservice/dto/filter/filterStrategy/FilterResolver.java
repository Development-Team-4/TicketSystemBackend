package development.team.ticketsystem.ticketservice.dto.filter.filterStrategy;

import development.team.ticketsystem.ticketservice.UserRole;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class FilterResolver {

    private final List<FilterBuilder> filterBuilders;

    public FilterBuilder resolve(UserRole role) {
        return filterBuilders.stream()
                .filter(builder -> builder.supports(role))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("No filter builder found for role: " + role));
    }
}