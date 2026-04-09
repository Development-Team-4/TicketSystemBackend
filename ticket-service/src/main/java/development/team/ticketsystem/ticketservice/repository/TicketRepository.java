package development.team.ticketsystem.ticketservice.repository;

import development.team.ticketsystem.ticketservice.dto.statistics.CategoryStatisticResponse;
import development.team.ticketsystem.ticketservice.dto.statistics.StatusStatisticResponse;
import development.team.ticketsystem.ticketservice.dto.statistics.TopicStatisticResponse;
import development.team.ticketsystem.ticketservice.entity.TicketEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.UUID;

public interface TicketRepository extends
        JpaRepository<TicketEntity, UUID>,
        JpaSpecificationExecutor<TicketEntity> {

    @Query(value = """
            SELECT t.status, COUNT(t.id)
            FROM tickets t
            GROUP BY t.status
            """, nativeQuery = true)
    List<StatusStatisticResponse> countByStatus();

    @Query(value = """
            SELECT t.category_id, COUNT(t.id)
            FROM tickets t
            GROUP BY t.category_id
            """, nativeQuery = true)
    List<CategoryStatisticResponse> countByCategory();

    @Query(value = """
            SELECT c.topic_id, COUNT(t.id)
            FROM tickets t
            JOIN categories c ON t.category_id = c.id
            GROUP BY c.topic_id
            """, nativeQuery = true)
    List<TopicStatisticResponse> countByTopicIds();

}