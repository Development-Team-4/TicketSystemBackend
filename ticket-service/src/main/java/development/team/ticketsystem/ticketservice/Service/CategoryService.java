package development.team.ticketsystem.ticketservice.Service;


import development.team.ticketsystem.ticketservice.DTO.categories.AssignStaffRequest;
import development.team.ticketsystem.ticketservice.DTO.categories.CategoryResponse;
import development.team.ticketsystem.ticketservice.DTO.categories.CreateCategoryRequest;
import development.team.ticketsystem.ticketservice.Entity.CategoryEntity;
import development.team.ticketsystem.ticketservice.Exceptions.AccessDeniedException;
import development.team.ticketsystem.ticketservice.Mappers.CategoryMapper;
import development.team.ticketsystem.ticketservice.Repository.CategoryRepository;
import development.team.ticketsystem.ticketservice.UserRole;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@RequiredArgsConstructor
@Service
public class CategoryService {

    private final CategoryRepository repository;
    private final CategoryStaffService categoryStaffService;
    private final CategoryMapper mapper;

    public List<CategoryResponse> getByTopic(UUID topicId) {
        return repository.findByTopicId(topicId)
                .stream()
                .map(mapper::toResponse)
                .toList();
    }

    public CategoryResponse getById(UUID id) throws EntityNotFoundException {
        return mapper.toResponse(repository.findById(id)
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
        CategoryEntity saved = repository.save(entity);
        return mapper.toResponse(saved);
    }

    public CategoryResponse update(UserRole role, UUID id, CreateCategoryRequest request)
            throws AccessDeniedException, EntityNotFoundException {
        if (role != UserRole.ADMIN) {
            throw new AccessDeniedException("Only ADMIN update category");
        }

        CategoryEntity existing = repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Category not found"));

        existing.setName(request.getName())
                .setDescription(request.getDescription());
        CategoryEntity entity = repository.save(existing);

        return mapper.toResponse(entity);
    }

    public void assignStaffToCategory(UserRole role,
                                      UUID id,
                                      AssignStaffRequest request
    ) throws AccessDeniedException {
        if (!role.equals(UserRole.ADMIN)) {
            throw new AccessDeniedException("Only admin can assign staff");
        }
        categoryStaffService.assignStaff(id, request.getCategoryId());
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

}