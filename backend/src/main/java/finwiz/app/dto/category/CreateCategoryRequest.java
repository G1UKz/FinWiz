package finwiz.app.dto.category;

import finwiz.app.entity.category.CategoryType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record CreateCategoryRequest(
    @NotBlank String name,
    @NotNull CategoryType type,
    String color,
    String icon
) {}
