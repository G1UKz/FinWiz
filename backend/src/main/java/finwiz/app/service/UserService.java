package finwiz.app.service;

import finwiz.app.dto.CreateUserRequest;
import finwiz.app.dto.UserResponse;
import finwiz.app.entity.User;
import finwiz.app.exception.ConflictException;
import finwiz.app.exception.ResourceNotFoundException;
import finwiz.app.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    @Transactional
    public UserResponse createUser(CreateUserRequest request) {
        if (userRepository.existsByEmail(request.email())) {
            throw new ConflictException("Email already registered");
        }

        User user = User.builder()
                .email(request.email())
                .passwordHash(passwordEncoder.encode(request.password()))
                .name(request.name())
                .build();

        User saved = userRepository.save(user);
        return new UserResponse(saved.getId(), saved.getEmail(), saved.getName(), saved.getCreatedAt());
    }

    @Transactional(readOnly = true)
    public UserResponse getUser(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
        return new UserResponse(user.getId(), user.getEmail(), user.getName(), user.getCreatedAt());
    }
}
