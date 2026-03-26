package development.team.ticketsystem.ticketservice.Service;

import development.team.ticketsystem.ticketservice.Entity.CategoryStaffEntity;
import development.team.ticketsystem.ticketservice.Exceptions.InvalidStateException;
import development.team.ticketsystem.ticketservice.Repository.CategoryRepository;
import development.team.ticketsystem.ticketservice.Repository.CategoryStaffRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@RequiredArgsConstructor
@Service
public class CategoryStaffService {

    private final CategoryStaffRepository repository;
    private final CategoryRepository categoryRepository;

    public List<CategoryStaffEntity> getByUser(UUID userId) {
        return repository.findByUserId(userId);
    }

    public boolean isUserInCategory(UUID userId, UUID categoryId) {
        return repository.existsByCategoryIdAndUserId(categoryId, userId);
    }

    public void assignStaff(UUID categoryId, UUID staffId) throws InvalidStateException, EntityNotFoundException {

        if (repository.existsByCategoryIdAndUserId(categoryId, staffId)) {
            throw new InvalidStateException("Staff already assigned to this category");
        }

        categoryRepository.findById(categoryId)
                .orElseThrow(() -> new EntityNotFoundException("Category not found"));

        CategoryStaffEntity entity = CategoryStaffEntity.builder()
                .categoryId(categoryId)
                .userId(staffId)
                .build();

        repository.save(entity);
    }

    public void removeStaff(UUID categoryId, UUID userId) throws RuntimeException {

        if (!repository.existsByCategoryIdAndUserId(categoryId, userId)) {
            throw new RuntimeException("Staff is not assigned to this category");
        }

        repository.deleteByCategoryIdAndUserId(categoryId, userId);
    }

}