package finwiz.app.repository.user;

import finwiz.app.entity.user.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class UserRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private UserRepository userRepository;

    @Test
    void shouldFindUserByEmail() {
        User user = new User();
        user.setEmail("ivan@finwiz.dev");
        user.setPasswordHash("$2a$10$hashed");
        user.setName("Иван Киселев");
        entityManager.persistAndFlush(user);

        Optional<User> found = userRepository.findByEmail("ivan@finwiz.dev");

        assertThat(found).isPresent();
        assertThat(found.get().getName()).isEqualTo("Иван Киселев");
    }

    @Test
    void shouldReturnEmptyWhenEmailNotFound() {
        Optional<User> found = userRepository.findByEmail("nobody@finwiz.dev");
        assertThat(found).isEmpty();
    }
}
