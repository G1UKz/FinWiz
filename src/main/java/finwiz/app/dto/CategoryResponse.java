package finwiz.app.dto;

import finwiz.app.entity.enums.CategoryType;
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
