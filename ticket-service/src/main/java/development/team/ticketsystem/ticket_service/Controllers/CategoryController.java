package development.team.ticketsystem.ticket_service.Controllers;


import development.team.ticketsystem.ticket_service.DTO.categories.CategoryResponse;
import development.team.ticketsystem.ticket_service.DTO.categories.CreateCategoryRequest;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/categories")
public class CategoryController {

    @GetMapping
    public List<CategoryResponse> getCategories() {
        return List.of();
    }

    @GetMapping("/{id}")
    public CategoryResponse getCategory(@PathVariable UUID id) {
        return new CategoryResponse();
    }

    @PostMapping
    public CategoryResponse createCategory(@RequestBody CreateCategoryRequest request) {
        return new CategoryResponse();
    }

    @PatchMapping("/{id}")
    public CategoryResponse updateCategory(
            @PathVariable UUID id,
            @RequestBody CreateCategoryRequest request
    ) {
        return new CategoryResponse();
    }

}
