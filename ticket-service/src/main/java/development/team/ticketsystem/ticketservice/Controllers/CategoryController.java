package development.team.ticketsystem.ticketservice.Controllers;


import development.team.ticketsystem.ticketservice.DTO.categories.CategoryResponse;
import development.team.ticketsystem.ticketservice.DTO.categories.CreateCategoryRequest;
import development.team.ticketsystem.ticketservice.Entity.CategoryEntity;
import development.team.ticketsystem.ticketservice.Service.CategoryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/topic/{topicId}/category")
@Tag(name = "Categories", description = "Управление категориями")
public class CategoryController {

    private final CategoryService service;

    public CategoryController(CategoryService service) {
        this.service = service;
    }


    @Operation(summary = "Получить все категории темы")
    @GetMapping
    public List<CategoryResponse> getCategories(
            @Parameter(description = "ID темы", example = "550e8400-e29b-41d4-a716-446655440000")
            @PathVariable UUID topicId
    ) {
        return service.getByTopic(topicId)
                .stream()
                .map(this::toResponse)
                .toList();
    }

    @Operation(summary = "Получить категорию по ID")
    @GetMapping("/{id}")
    public CategoryResponse getCategory(
            @Parameter(description = "ID темы", example = "550e8400-e29b-41d4-a716-446655440000")
            @PathVariable UUID topicId,

            @Parameter(description = "ID категории", example = "550e8400-e29b-41d4-a716-446655440000")
            @PathVariable UUID id
    ) {
        return toResponse(service.getById(id));
    }

    @Operation(summary = "Создать категорию")
    @PostMapping
    public CategoryResponse createCategory(
            @Parameter(description = "ID темы", example = "550e8400-e29b-41d4-a716-446655440000")
            @PathVariable UUID topicId,

            @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "Данные для создания категории")
            @RequestBody CreateCategoryRequest request
    ) {
        CategoryEntity entity = new CategoryEntity();
        entity.setTopicId(topicId);
        entity.setName(request.getName());
        entity.setDescription(request.getDescription());

        return toResponse(service.create(entity));
    }

    @Operation(summary = "Обновить категорию")
    @PutMapping("/{id}")
    public CategoryResponse updateCategory(
            @Parameter(description = "ID темы", example = "550e8400-e29b-41d4-a716-446655440000")
            @PathVariable UUID topicId,

            @Parameter(description = "ID категории", example = "550e8400-e29b-41d4-a716-446655440000")
            @PathVariable UUID id,

            @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "Данные для обновления категории")
            @RequestBody CreateCategoryRequest request
    ) {
        CategoryEntity updated = new CategoryEntity();
        updated.setName(request.getName());
        updated.setDescription(request.getDescription());

        return toResponse(service.update(id, updated));
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
