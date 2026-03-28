package development.team.ticketsystem.ticketservice.repository;

import development.team.ticketsystem.ticketservice.entity.TopicEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface TopicRepository extends JpaRepository<TopicEntity, UUID> {
}
