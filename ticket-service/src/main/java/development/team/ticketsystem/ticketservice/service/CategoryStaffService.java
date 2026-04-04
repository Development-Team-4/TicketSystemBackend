package development.team.ticketsystem.ticketservice.service;

import development.team.ticketsystem.ticketservice.entity.CategoryStaffEntity;
import development.team.ticketsystem.ticketservice.exceptions.InvalidStateException;
import development.team.ticketsystem.ticketservice.repository.CategoryRepository;
import development.team.ticketsystem.ticketservice.repository.CategoryStaffRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
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

    @Transactional
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

    @Transactional
    public void removeStaff(UUID categoryId, UUID userId) throws InvalidStateException {
        CategoryStaffEntity assignment = repository
                .findByCategoryIdAndUserId(categoryId, userId)
                .orElseThrow(() -> new InvalidStateException("Staff is not assigned to this category"));

        repository.delete(assignment);
    }

}