package finwiz.app.controller.user;

import com.fasterxml.jackson.databind.ObjectMapper;
import finwiz.app.dto.user.LoginRequest;
import finwiz.app.dto.user.RegisterRequest;
import finwiz.app.entity.user.User;
import finwiz.app.repository.user.UserRepository;
import finwiz.app.security.JwtService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(AuthController.class)
@AutoConfigureMockMvc(addFilters = false)  // отключаем JWT filter для юнит-теста
class AuthControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private UserRepository userRepository;

    @MockitoBean
    private PasswordEncoder passwordEncoder;

    @MockitoBean
    private JwtService jwtService;

    @Test
    void shouldLoginAndReturnToken() throws Exception {
        User user = new User();
        user.setId(1L);
        user.setEmail("ivan@finwiz.dev");
        user.setPasswordHash("$2a$10$encoded");
        user.setName("Ivan");

        when(userRepository.findByEmail("ivan@finwiz.dev")).thenReturn(Optional.of(user));
        when(passwordEncoder.matches("secret123", "$2a$10$encoded")).thenReturn(true);
        when(jwtService.generateToken(1L, "ivan@finwiz.dev")).thenReturn("mock-jwt-token-123");

        LoginRequest req = new LoginRequest("ivan@finwiz.dev", "secret123");

        mockMvc.perform(post("/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(req)))
            .andDo(print())
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.accessToken").value("mock-jwt-token-123"))
            .andExpect(jsonPath("$.tokenType").value("Bearer"))
            .andExpect(jsonPath("$.userId").value(1));
    }

    @Test
    void shouldRegisterNewUser() throws Exception {
        when(userRepository.existsByEmail("new@finwiz.dev")).thenReturn(false);
        when(passwordEncoder.encode("password")).thenReturn("$2a$10$hash");
        when(userRepository.save(any(User.class))).thenAnswer(inv -> {
            User u = inv.getArgument(0);
            u.setId(5L);
            return u;
        });
        when(jwtService.generateToken(5L, "new@finwiz.dev")).thenReturn("new-jwt-token");

        RegisterRequest req = new RegisterRequest("new@finwiz.dev", "password", "New User");

        mockMvc.perform(post("/auth/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(req)))
            .andDo(print())
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.accessToken").value("new-jwt-token"))
            .andExpect(jsonPath("$.userId").value(5));
    }

    @Test
    void shouldReturn401ForInvalidPassword() throws Exception {
        User user = new User();
        user.setId(1L);
        user.setEmail("ivan@finwiz.dev");
        user.setPasswordHash("$2a$10$encoded");
        user.setName("Ivan");

        when(userRepository.findByEmail("ivan@finwiz.dev")).thenReturn(Optional.of(user));
        when(passwordEncoder.matches("wrong", "$2a$10$encoded")).thenReturn(false);

        LoginRequest req = new LoginRequest("ivan@finwiz.dev", "wrong");

        mockMvc.perform(post("/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(req)))
            .andDo(print())
            .andExpect(status().isUnauthorized());
    }
}
