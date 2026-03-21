package development.team.ticketsystem.ticketservice.Service;


import development.team.ticketsystem.ticketservice.DTO.categories.CategoryResponse;
import development.team.ticketsystem.ticketservice.DTO.categories.CreateCategoryRequest;
import development.team.ticketsystem.ticketservice.Entity.CategoryEntity;
import development.team.ticketsystem.ticketservice.Repository.CategoryRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class CategoryService {

    private final CategoryRepository repository;

    public CategoryService(CategoryRepository repository) {
        this.repository = repository;
    }

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
        CategoryEntity entity = new CategoryEntity();
        entity.setTopicId(topicId);
        entity.setName(request.getName());
        entity.setDescription(request.getDescription());
        CategoryEntity saved = repository.save(entity);
        return toResponse(saved);
    }

    public CategoryResponse update(UUID id, CreateCategoryRequest request) {
        CategoryEntity existing = repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Category not found"));

        existing.setName(request.getName());
        existing.setDescription(request.getDescription());
        CategoryEntity entity = repository.save(existing);

        return toResponse(entity);
    }

    // mapper - ИСПОЛЬЗОВАТЬ БИБЛИОТЕКУ mapstruct
    private CategoryResponse toResponse(CategoryEntity entity) {
        CategoryResponse response = new CategoryResponse();
        response.setId(entity.getId());
        response.setTopicId(entity.getTopicId());
        response.setName(entity.getName());
        response.setDescription(entity.getDescription());
        return response;
    }
}