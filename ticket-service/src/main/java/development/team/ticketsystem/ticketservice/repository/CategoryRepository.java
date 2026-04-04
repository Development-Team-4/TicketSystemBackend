package development.team.ticketsystem.ticketservice.repository;

import development.team.ticketsystem.ticketservice.entity.CategoryEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface CategoryRepository extends JpaRepository<CategoryEntity, UUID> {

    List<CategoryEntity> findByTopicId(UUID topicId);
}