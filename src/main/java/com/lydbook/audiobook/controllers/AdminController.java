package com.lydbook.audiobook.controllers;

import com.lydbook.audiobook.entity.User;
import com.lydbook.audiobook.repository.BookRepository;
import com.lydbook.audiobook.repository.ProgressRepository;
import com.lydbook.audiobook.repository.UserRepository;
import com.lydbook.audiobook.services.UserService;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class AdminController {
    private final UserRepository userRepository;
    private final UserService userService;
    private final ProgressRepository progressRepository;
    private final BookRepository bookRepository;

    public AdminController(UserRepository userRepository, UserService userService, ProgressRepository progressRepository, BookRepository bookRepository) {
        this.userRepository = userRepository;
        this.userService = userService;
        this.progressRepository = progressRepository;
        this.bookRepository = bookRepository;
    }

    @GetMapping("/admin/upload")
    public String uploadView() {
        return "/admin/upload";
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
        return "redirect:/admin/users";
    }
}
