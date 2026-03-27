package development.team.ticketsystem.ticketservice.repository;


import development.team.ticketsystem.ticketservice.entity.CategoryStaffEntity;
import development.team.ticketsystem.ticketservice.entity.CategoryStaffId;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface CategoryStaffRepository
        extends JpaRepository<CategoryStaffEntity, CategoryStaffId> {

    List<CategoryStaffEntity> findByUserId(UUID userId);

    List<CategoryStaffEntity> findByCategoryId(UUID categoryId);

    boolean existsByCategoryIdAndUserId(UUID categoryId, UUID userId);

    void deleteByCategoryIdAndUserId(UUID categoryId, UUID userId);
}