package development.team.ticketsystem.ticket_service.Controllers;


import development.team.ticketsystem.ticket_service.DTO.categories.CategoryResponse;
import development.team.ticketsystem.ticket_service.DTO.categories.CreateCategoryRequest;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/topic/{topicId}/category")
public class CategoryController {

    @GetMapping
    public List<CategoryResponse> getCategories(
            @PathVariable UUID topicId
    ) {
        return List.of();
    }

    @GetMapping("/{id}")
    public CategoryResponse getCategory(
            @PathVariable UUID topicId,
            @PathVariable UUID id
    ) {
        return new CategoryResponse();
    }

    @PostMapping
    public CategoryResponse createCategory(
            @PathVariable UUID topicId,
            @RequestBody CreateCategoryRequest request
    ) {
        return new CategoryResponse();
    }

    @PutMapping("/{id}")
    public CategoryResponse updateCategory(
            @PathVariable UUID topicId,
            @PathVariable UUID id,
            @RequestBody CreateCategoryRequest request
    ) {
        return new CategoryResponse();
    }

}
