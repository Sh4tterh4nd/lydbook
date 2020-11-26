package ch.fhnw.webec.controllers;

import ch.fhnw.webec.entity.User;
import ch.fhnw.webec.repository.UserRepository;
import ch.fhnw.webec.services.UserService;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class AdminController {
    private final UserRepository userRepository;
    private final UserService userService;

    public AdminController(UserRepository userRepository, UserService userService) {
        this.userRepository = userRepository;
        this.userService = userService;
    }

    @GetMapping("/admin/upload")
    public String uploadView() {
        return "/admin/upload";
    }

    @GetMapping("/admin/permissions")
    public String permissionView() {
        return "/admin/permissions";
    }

    @GetMapping("/admin/users")
    public String usersView(Model model) {
        model.addAttribute("users", userRepository.findAllByOrderByUsernameAsc());
        return "/admin/users";
    }

    @PostMapping(path = "/admin/users", consumes = {MediaType.APPLICATION_FORM_URLENCODED_VALUE})
    public String createUser(User user, Model model) {
        userService.createUser(user);
        model.addAttribute("users", userRepository.findAllByOrderByUsernameAsc());
        return "/admin/users";
    }


    @DeleteMapping("/admin/users/{id}")
    public String deleteUser(@PathVariable("id") Long id) {
        return "/admin/users";
    }
}
