package development.team.ticketsystem.ticketservice.service;

import development.team.ticketsystem.ticketservice.TicketStatus;
import development.team.ticketsystem.ticketservice.repository.TicketRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.EnumMap;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class StatisticsService {

    private final TicketRepository ticketRepository;

    public Map<TicketStatus, Long> getStatusStats() {
        Map<TicketStatus, Long> result = new EnumMap<>(TicketStatus.class);

        for (TicketStatus status : TicketStatus.values()) {
            result.put(status, 0L);
        }

        ticketRepository.countByStatus()
                .forEach(row -> result.put(
                        (TicketStatus) row[0],
                        (Long) row[1]
                ));

        return result;
    }

    public Map<UUID, Long> getCategoryStats() {
        return ticketRepository.countByCategory()
                .stream()
                .collect(Collectors.toMap(
                        row -> (UUID) row[0],
                        row -> (Long) row[1]
                ));
    }

    public Map<UUID, Long> getTopicStats() {
        return ticketRepository.countByTopicIds()
                .stream()
                .collect(Collectors.toMap(
                        row -> (UUID) row[0],
                        row -> ((Number) row[1]).longValue()
                ));
    }
}