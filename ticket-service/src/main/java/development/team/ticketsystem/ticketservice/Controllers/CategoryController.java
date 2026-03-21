package development.team.ticketsystem.ticketservice.Controllers;


import development.team.ticketsystem.ticketservice.DTO.categories.CategoryResponse;
import development.team.ticketsystem.ticketservice.DTO.categories.CreateCategoryRequest;
import development.team.ticketsystem.ticketservice.Service.CategoryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RequiredArgsConstructor
@RestController
@Tag(name = "Categories",
        description = "Управление категориями в системе поддержки. Категории группируют тикеты по тематикам внутри определенной темы.")
public class CategoryController {

    private final CategoryService service;


    @GetMapping("/topic/{topicId}/category")
    public List<CategoryResponse> getCategories(
            @Parameter(
                    description = "Уникальный идентификатор темы, для которой запрашиваются категории",
                    example = "550e8400-e29b-41d4-a716-446655440000",
                    required = true
            )
            @PathVariable UUID topicId
    ) {
        return service.getByTopic(topicId);
    }



    @GetMapping("/topic/{topicId}/category/{id}")
    public CategoryResponse getCategory(
            @Parameter(
                    description = "Уникальный идентификатор темы (родительской сущности для категории)",
                    example = "550e8400-e29b-41d4-a716-446655440000",
                    required = true
            )
            @PathVariable UUID topicId,

            @Parameter(
                    description = "Уникальный идентификатор категории, которую необходимо получить",
                    example = "550e8400-e29b-41d4-a716-446655440001",
                    required = true
            )
            @PathVariable UUID id
    ) {
        return service.getById(id);
    }



    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/topic/{topicId}/category")
    public CategoryResponse createCategory(
            @Parameter(description = "ID темы", example = "550e8400-e29b-41d4-a716-446655440000")
            @PathVariable UUID topicId,

            @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "Данные для создания категории")
            @RequestBody CreateCategoryRequest request
    ) {
        return service.create(topicId, request);
    }

    @Operation(summary = "Обновить категорию")
    @PutMapping("/topic/{topicId}/category/{id}")
    public CategoryResponse updateCategory(
            @Parameter(description = "ID темы", example = "550e8400-e29b-41d4-a716-446655440000")
            @PathVariable UUID topicId,

            @Parameter(description = "ID категории", example = "550e8400-e29b-41d4-a716-446655440000")
            @PathVariable UUID id,

            @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "Данные для обновления категории")
            @RequestBody CreateCategoryRequest request
    ) {
        return service.update(id, request);
    }

}
