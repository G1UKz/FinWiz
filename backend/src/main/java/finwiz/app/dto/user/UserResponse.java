package finwiz.app.dto.user;

import java.time.LocalDateTime;

public record UserResponse(
    Long id,
    String email,
    String name,
    LocalDateTime createdAt
) {}
