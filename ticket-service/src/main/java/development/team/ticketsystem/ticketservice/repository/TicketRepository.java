package development.team.ticketsystem.ticketservice.repository;

import development.team.ticketsystem.ticketservice.entity.TicketEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.UUID;

public interface TicketRepository extends
        JpaRepository<TicketEntity, UUID>,
        JpaSpecificationExecutor<TicketEntity> {

    @Query("""
            SELECT t.status, COUNT(t)
            FROM TicketEntity t
            GROUP BY t.status
            """)
    List<Object[]> countByStatus();

    @Query("""
            SELECT t.categoryId, COUNT(t)
            FROM TicketEntity t
            GROUP BY t.categoryId
            """)
    List<Object[]> countByCategory();

    @Query(value = """
            SELECT c.topic_id, COUNT(t.id)
            FROM tickets t
            JOIN categories c ON t.category_id = c.id
            GROUP BY c.topic_id
            """, nativeQuery = true)
    List<Object[]> countByTopicIds();

}