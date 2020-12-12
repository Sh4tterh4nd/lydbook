package ch.fhnw.webec.services;

import ch.fhnw.webec.dao.DAOPassword;
import ch.fhnw.webec.entity.User;
import ch.fhnw.webec.entity.UserRole;
import ch.fhnw.webec.repository.TagRepository;
import ch.fhnw.webec.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;

@Service
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final TagRepository tagRepository;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder, TagRepository tagRepository) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.tagRepository = tagRepository;
    }

    @Transactional
    public void createUser(User user) {
        user.setRole(UserRole.USER);
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.addTag(tagRepository.findOrCreateFirstByName("Audiobook", false));
        userRepository.save(user);
    }

    @Transactional
    public void deleteUser(Long id) {
        userRepository.deleteUserById(id);
    }

    public User getUser(Long id) {
        User user = userRepository.findUserById(id);
        user.setPassword("");
        user.getProgressList().clear();
        return user;
    }

    @Transactional
    public void updateUser(User updatedUser) {
        User user = userRepository.findUserById(updatedUser.getId());
        if (!updatedUser.getPassword().isEmpty()) {
            user.setPassword(passwordEncoder.encode(updatedUser.getPassword()));
        }
        user.getTags().clear();
        updatedUser.getTags().forEach(t -> user.addTag(t));

        userRepository.save(user);
    }

    public boolean updatePassword(String username, DAOPassword password) {
        User user = userRepository.findUserByUsername(username);
        if (passwordEncoder.matches(password.getCurrentPassword(), user.getPassword())) {
            user.setPassword(passwordEncoder.encode(password.getNewPassword()));
            userRepository.save(user);
            return true;
        }
        return false;
    }
}
