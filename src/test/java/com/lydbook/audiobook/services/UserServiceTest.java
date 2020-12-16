package com.lydbook.audiobook.services;

import com.lydbook.audiobook.dao.DAOPassword;
import com.lydbook.audiobook.entity.User;
import com.lydbook.audiobook.repository.UserRepository;
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
        User test1 = userRepository.findUserByUsername("test1");
        userService.deleteUser(test1.getId());
        assertTrue(userRepository.findUserByUsername("test1") == null);
    }

    @Test
    void getUser() {
        User user = userRepository.findUserByUsername("admin");
        assertEquals(user.getUsername(), "admin");
        assertTrue(passwordEncoder.matches("admin", user.getPassword()));
        assertEquals(user.getId(), 10000L);
    }

    @Test
    void getUserById() {
        User user = userService.getUser(10000L);

        assertEquals(user.getUsername(), "admin");
    }

    @Test
    void updateUser() {
        User test6 = userRepository.findUserByUsername("test6");
        test6.getTags();
        test6.getTags().clear();
        test6.setPassword("");
        userService.updateUser(test6);
        assertTrue(userRepository.findUserByUsername("test6").getTags().size() == 0);
    }

    @Test
    void updatePassword() {
        DAOPassword daoPassword = new DAOPassword();
        daoPassword.setCurrentPassword("test6");
        daoPassword.setNewPassword("test6+");

        userService.updatePassword("test6", daoPassword);
        User test6 = userRepository.findUserByUsername("test6");
        assertTrue(passwordEncoder.matches("test6+", test6.getPassword()));
    }
    @Test
    void updatePasswordWithWrongOldPassword() {
        DAOPassword daoPassword = new DAOPassword();
        daoPassword.setCurrentPassword("test5--");
        daoPassword.setNewPassword("test5+");

        userService.updatePassword("test5", daoPassword);
        User test5 = userRepository.findUserByUsername("test5");
        assertTrue(passwordEncoder.matches("test5", test5.getPassword()));
    }
}