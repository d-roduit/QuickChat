package ch.droduit.quickchat;

import ch.droduit.quickchat.domain.User;
import ch.droduit.quickchat.domain.UserRepository;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * <b>Test class for {@link UserRepository}.</b>
 */
@ExtendWith(SpringExtension.class)
@SpringBootTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class UserRepositoryTests {

    @Autowired
    private UserRepository userRepository;

    private static User testUser;

    @Test
    @Order(1)
    public void createTestUser() {
        testUser = new User(
                "testUser@quickchat.com",
                "$2a$12$BViY6QKJwfxs/8JQ52WVcejSaEfP783LYLtT8ZRyjPp6YWCYtmQ.C",
                "USER");
        userRepository.save(testUser);
        assertThat(testUser.getId()).isNotNull();
    }

    @Test
    @Order(2)
    public void findByEmailShouldReturnUser() {
        User testUserFetchedFromDB = userRepository.findByEmail("testUser@quickchat.com");
        assertThat(testUserFetchedFromDB).isNotNull();
        assertThat(testUserFetchedFromDB.getId()).isEqualTo(testUser.getId());
        assertThat(testUserFetchedFromDB.getEmail()).isEqualTo(testUser.getEmail());
        assertThat(BCrypt.checkpw("testUser", testUserFetchedFromDB.getPasswordHash())).isTrue();
        assertThat(testUserFetchedFromDB.getRole()).isEqualTo(testUser.getRole());
    }

    @Test
    @Order(3)
    public void deleteTestUser() {
        userRepository.delete(testUser);
        User testUserFetchedFromDB = userRepository.findByEmail("testUser@quickchat.com");
        assertThat(testUserFetchedFromDB).isNull();
    }
}
