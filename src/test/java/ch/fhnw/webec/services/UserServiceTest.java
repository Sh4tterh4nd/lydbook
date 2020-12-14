package ch.fhnw.webec.services;

import ch.fhnw.webec.entity.Tag;
import ch.fhnw.webec.entity.User;
import ch.fhnw.webec.repository.UserRepository;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;

import static org.junit.jupiter.api.Assertions.*;

@EnableAutoConfiguration
@SpringBootTest()
@ActiveProfiles("test")
class UserServiceTest {

    private final String USERNAME = "test_user";
    private final String USERNAME_PW = "test_user";
    private final String USERNAME_PW_UPDATE = "test_user_up";
    private final String TAG = "test";

    @Autowired
    private UserService userService;

    @Autowired
    private UserRepository userRepository;

    private PasswordEncoder passwordEncoder = new BCryptPasswordEncoder(12);

    @Test
    void createUser() {
        User user = new User();
        user.setUsername(USERNAME);
        user.setPassword(USERNAME_PW);
        userService.createUser(user);
        User dbUser = userRepository.findUserByUsername(USERNAME);
        assertTrue(passwordEncoder.matches(USERNAME_PW, dbUser.getPassword()));
        assertEquals(dbUser.getUsername(), user.getUsername());
    }

    @Test
    void deleteUser() {
    }

    @Test
    void getUser() {
    }

    @Test
    void updateUser() {
    }

    @Test
    void updatePassword() {

    }
}