package development.team.ticketsystem.ticketservice.Controllers;


import development.team.ticketsystem.ticketservice.DTO.categories.CategoryResponse;
import development.team.ticketsystem.ticketservice.DTO.categories.CreateCategoryRequest;
import development.team.ticketsystem.ticketservice.Entity.CategoryEntity;
import development.team.ticketsystem.ticketservice.Service.CategoryService;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/topic/{topicId}/category")
public class CategoryController {

    private final CategoryService service;

    public CategoryController(CategoryService service) {
        this.service = service;
    }


    @GetMapping
    public List<CategoryResponse> getCategories(
            @PathVariable UUID topicId
    ) {
        return service.getByTopic(topicId)
                .stream()
                .map(this::toResponse)
                .toList();
    }

    @GetMapping("/{id}")
    public CategoryResponse getCategory(
            @PathVariable UUID topicId,
            @PathVariable UUID id
    ) {
        return toResponse(service.getById(id));
    }

    @PostMapping
    public CategoryResponse createCategory(
            @PathVariable UUID topicId,
            @RequestBody CreateCategoryRequest request
    ) {
        CategoryEntity entity = new CategoryEntity();
        entity.setTopicId(topicId);
        entity.setName(request.getName());
        entity.setDescription(request.getDescription());

        return toResponse(service.create(entity));
    }

    @PutMapping("/{id}")
    public CategoryResponse updateCategory(
            @PathVariable UUID topicId,
            @PathVariable UUID id,
            @RequestBody CreateCategoryRequest request
    ) {
        CategoryEntity updated = new CategoryEntity();
        updated.setName(request.getName());
        updated.setDescription(request.getDescription());

        return toResponse(service.update(id, updated));
    }

    // mapper
    private CategoryResponse toResponse(CategoryEntity entity) {
        CategoryResponse response = new CategoryResponse();
        response.setId(entity.getId());
        response.setTopicId(entity.getTopicId());
        response.setName(entity.getName());
        response.setDescription(entity.getDescription());
        return response;
    }
}
