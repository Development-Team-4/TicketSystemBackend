package development.team.ticketsystem.ticketservice.service;

import development.team.ticketsystem.ticketservice.TicketStatus;
import development.team.ticketsystem.ticketservice.dto.statistics.CategoryStatisticResponse;
import development.team.ticketsystem.ticketservice.dto.statistics.StatusStatisticResponse;
import development.team.ticketsystem.ticketservice.dto.statistics.TopicStatisticResponse;
import development.team.ticketsystem.ticketservice.repository.TicketRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class StatisticsService {

    private final TicketRepository ticketRepository;

    public List<StatusStatisticResponse> getStatusStats() {
        return ticketRepository.countByStatus();
    }

    public List<CategoryStatisticResponse> getCategoryStats() {
        return ticketRepository.countByCategory();
    }

    public List<TopicStatisticResponse> getTopicStats() {
        return ticketRepository.countByTopicIds();
    }
}