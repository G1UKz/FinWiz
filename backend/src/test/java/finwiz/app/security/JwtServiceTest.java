package finwiz.app.security;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.Base64;

import static org.assertj.core.api.Assertions.assertThat;

class JwtServiceTest {

    private final JwtService jwtService = new JwtService();

    @BeforeEach
    void setUp() {
        String raw = "this-is-a-test-secret-that-is-long-enough-for-hs256!!";
        String base64 = Base64.getEncoder().encodeToString(raw.getBytes());

        ReflectionTestUtils.setField(jwtService, "secret", base64);
        ReflectionTestUtils.setField(jwtService, "expiration", 3600000L);
    }

    @Test
    void shouldGenerateAndValidateToken() {
        Long userId = 1L;
        String email = "ivan@finwiz.dev";

        String token = jwtService.generateToken(userId, email);

        assertThat(token).isNotBlank();
        assertThat(jwtService.isTokenValid(token, email)).isTrue();
        assertThat(jwtService.extractEmail(token)).isEqualTo(email);
        assertThat(jwtService.extractUserId(token)).isEqualTo(userId);
    }

    @Test
    void shouldRejectTokenWithWrongEmail() {
        String token = jwtService.generateToken(1L, "ivan@finwiz.dev");

        assertThat(jwtService.isTokenValid(token, "hacker@finwiz.dev")).isFalse();
    }

    @Test
    void shouldRejectExpiredToken() throws Exception {
        ReflectionTestUtils.setField(jwtService, "expiration", 1L);
        String token = jwtService.generateToken(1L, "ivan@finwiz.dev");

        Thread.sleep(10);

        assertThat(jwtService.isTokenValid(token, "ivan@finwiz.dev")).isFalse();
    }
}
