package finwiz.app.dto;

public record AuthResponse(String accessToken, String tokenType, Long userId) {
}
