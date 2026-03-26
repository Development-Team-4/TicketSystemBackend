package development.team.ticketsystem.ticketservice.Service;


import development.team.ticketsystem.ticketservice.DTO.categories.AssignStaffRequest;
import development.team.ticketsystem.ticketservice.DTO.categories.CategoryResponse;
import development.team.ticketsystem.ticketservice.DTO.categories.CreateCategoryRequest;
import development.team.ticketsystem.ticketservice.Entity.CategoryEntity;
import development.team.ticketsystem.ticketservice.Exceptions.AccessDeniedException;
import development.team.ticketsystem.ticketservice.Repository.CategoryRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

import development.team.ticketsystem.ticketservice.UserRole;

@RequiredArgsConstructor
@Service
public class CategoryService {

    private final CategoryRepository repository;
    private final CategoryStaffService categoryStaffService;

    public List<CategoryResponse> getByTopic(UUID topicId) {
        return repository.findByTopicId(topicId)
                .stream()
                .map(this::toResponse)
                .toList();
    }

    public CategoryResponse getById(UUID id) {
        return toResponse(repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Category not found")));
    }

    public CategoryResponse create(UserRole role, UUID topicId, CreateCategoryRequest request) {
        if (role != UserRole.ADMIN) {
            throw new AccessDeniedException("Only ADMIN create category");
        }

        CategoryEntity entity = CategoryEntity.builder()
                .topicId(topicId)
                .name(request.getName())
                .description(request.getDescription())
                .build();
        CategoryEntity saved = repository.save(entity);
        return toResponse(saved);
    }

    public CategoryResponse update(UserRole role, UUID id, CreateCategoryRequest request) {
        if (role != UserRole.ADMIN) {
            throw new AccessDeniedException("Only ADMIN update category");
        }

        CategoryEntity existing = repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Category not found"));

        existing.setName(request.getName())
                .setDescription(request.getDescription());
        CategoryEntity entity = repository.save(existing);

        return toResponse(entity);
    }

    public void assignStaffToCategory(UserRole role,
                                      UUID id,
                                      AssignStaffRequest request)
    {
        if (!role.equals(UserRole.ADMIN)) {
            throw new AccessDeniedException("Only admin can assign staff");
        }
        categoryStaffService.assignStaff(id, request.getCategoryId());
    }

    public void removeStaff(UserRole role,
                            UUID categoryId,
                            UUID staffId) {
        if (role != UserRole.ADMIN) {
            throw new AccessDeniedException("Only ADMIN can remove staff");
        }

        categoryStaffService.removeStaff(categoryId, staffId);
    }


    // mapper - ИСПОЛЬЗОВАТЬ БИБЛИОТЕКУ mapstruct
    private CategoryResponse toResponse(CategoryEntity entity) {
        return CategoryResponse.builder()
                .id(entity.getId())
                .topicId(entity.getTopicId())
                .name(entity.getName())
                .description(entity.getDescription())
                .build();
    }


}