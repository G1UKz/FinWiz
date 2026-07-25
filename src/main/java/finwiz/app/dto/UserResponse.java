package finwiz.app.dto;

import java.time.LocalDateTime;

public record UserResponse(
    Long id,
    String email,
    String name,
    LocalDateTime createdAt
) {}
