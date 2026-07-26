package finwiz.app.dto.category;

import finwiz.app.entity.category.CategoryType;
import java.time.LocalDateTime;

public record CategoryResponse(
    Long id,
    String name,
    CategoryType type,
    String color,
    String icon,
    Boolean isSystem,
    LocalDateTime createdAt
) {}
