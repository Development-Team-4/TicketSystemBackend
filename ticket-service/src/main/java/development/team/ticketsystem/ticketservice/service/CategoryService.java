package development.team.ticketsystem.ticketservice.service;


import development.team.ticketsystem.ticketservice.UserRole;
import development.team.ticketsystem.ticketservice.dto.categories.AssignStaffRequest;
import development.team.ticketsystem.ticketservice.dto.categories.CategoryResponse;
import development.team.ticketsystem.ticketservice.dto.categories.CreateCategoryRequest;
import development.team.ticketsystem.ticketservice.entity.CategoryEntity;
import development.team.ticketsystem.ticketservice.exceptions.AccessDeniedException;
import development.team.ticketsystem.ticketservice.mappers.CategoryMapper;
import development.team.ticketsystem.ticketservice.repository.CategoryRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@RequiredArgsConstructor
@Service
public class CategoryService {

    private final CategoryRepository categoryRepository;
    private final CategoryStaffService categoryStaffService;
    private final CategoryMapper categoryMapper;

    public List<CategoryResponse> getByTopic(UUID topicId) {
        return categoryRepository.findByTopicId(topicId)
                .stream()
                .map(categoryMapper::toResponse)
                .toList();
    }

    public CategoryResponse getById(UUID id) throws EntityNotFoundException {
        return categoryMapper.toResponse(categoryRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Category not found")));
    }

    public CategoryResponse create(UserRole role, UUID topicId, CreateCategoryRequest request)
            throws AccessDeniedException {
        if (role != UserRole.ADMIN) {
            throw new AccessDeniedException("Only ADMIN create category");
        }

        CategoryEntity entity = CategoryEntity.builder()
                .topicId(topicId)
                .name(request.getName())
                .description(request.getDescription())
                .build();
        CategoryEntity saved = categoryRepository.save(entity);
        return categoryMapper.toResponse(saved);
    }

    @Transactional
    public CategoryResponse update(UserRole role, UUID id, CreateCategoryRequest request)
            throws AccessDeniedException, EntityNotFoundException {
        if (role != UserRole.ADMIN) {
            throw new AccessDeniedException("Only ADMIN update category");
        }

        CategoryEntity existing = categoryRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Category not found"));

        existing.setName(request.getName())
                .setDescription(request.getDescription());
        CategoryEntity entity = categoryRepository.save(existing);

        return categoryMapper.toResponse(entity);
    }

    public void assignStaffToCategory(UserRole role,
                                      UUID id,
                                      AssignStaffRequest request
    ) throws AccessDeniedException {
        if (!role.equals(UserRole.ADMIN)) {
            throw new AccessDeniedException("Only admin can assign staff");
        }
        categoryStaffService.assignStaff(id, request.getStaffId());
    }

    public void removeStaff(UserRole role,
                            UUID categoryId,
                            UUID staffId
    ) throws AccessDeniedException {
        if (!role.equals(UserRole.ADMIN)) {
            throw new AccessDeniedException("Only ADMIN can remove staff");
        }

        categoryStaffService.removeStaff(categoryId, staffId);
    }

    @Transactional
    public boolean isStaffAssignedToCategory(UUID categoryId, UUID staffId) {

        if (!categoryRepository.existsById(categoryId)) {
            throw new AccessDeniedException("Category with this ID does not exist");
        }

        return categoryStaffService.isUserInCategory(staffId, categoryId);
    }

}