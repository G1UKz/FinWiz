package finwiz.app.dto.user;

public record AuthResponse(String accessToken, String tokenType, Long userId) {
}
