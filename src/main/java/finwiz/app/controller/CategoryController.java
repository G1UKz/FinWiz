package finwiz.app.controller;

import finwiz.app.dto.CategoryResponse;
import finwiz.app.dto.CreateCategoryRequest;
import finwiz.app.service.CategoryService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class CategoryController {

    private final CategoryService categoryService;

    @PostMapping("/users/{userId}/categories")
    @ResponseStatus(HttpStatus.CREATED)
    public CategoryResponse createCategory(
            @PathVariable Long userId,
            @Valid @RequestBody CreateCategoryRequest request) {
        return categoryService.createCategory(userId, request);
    }

    @GetMapping("/users/{userId}/categories")
    public List<CategoryResponse> listCategories(@PathVariable Long userId) {
        return categoryService.listCategories(userId);
    }

    @GetMapping("/categories/{id}")
    public CategoryResponse getCategory(@PathVariable Long id) {
        return categoryService.getCategory(id);
    }
}
