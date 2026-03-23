package development.team.ticketsystem.ticketservice.Repository;

import development.team.ticketsystem.ticketservice.Entity.TopicEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface TopicRepository extends JpaRepository<TopicEntity, UUID> {
}
