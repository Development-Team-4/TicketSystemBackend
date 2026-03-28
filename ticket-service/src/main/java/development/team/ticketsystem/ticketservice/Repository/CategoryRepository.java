package development.team.ticketsystem.ticketservice.Repository;

import development.team.ticketsystem.ticketservice.Entity.CategoryEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface CategoryRepository extends JpaRepository<CategoryEntity, UUID> {

    List<CategoryEntity> findByTopicId(UUID topicId);
}