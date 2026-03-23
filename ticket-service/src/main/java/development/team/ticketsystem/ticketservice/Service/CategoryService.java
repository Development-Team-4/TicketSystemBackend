package development.team.ticketsystem.ticketservice.Service;


import development.team.ticketsystem.ticketservice.DTO.categories.CategoryResponse;
import development.team.ticketsystem.ticketservice.DTO.categories.CreateCategoryRequest;
import development.team.ticketsystem.ticketservice.Entity.CategoryEntity;
import development.team.ticketsystem.ticketservice.Repository.CategoryRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@RequiredArgsConstructor
@Service
public class CategoryService {

    private final CategoryRepository repository;

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

    public CategoryResponse create(UUID topicId, CreateCategoryRequest request) {
        CategoryEntity entity = CategoryEntity.builder()
                .topicId(topicId)
                .name(request.getName())
                .description(request.getDescription())
                .build();
        CategoryEntity saved = repository.save(entity);
        return toResponse(saved);
    }

    public CategoryResponse update(UUID id, CreateCategoryRequest request) {
        CategoryEntity existing = repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Category not found"));

        existing.setName(request.getName())
                .setDescription(request.getDescription());
        CategoryEntity entity = repository.save(existing);

        return toResponse(entity);
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