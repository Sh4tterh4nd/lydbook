package com.lydbook.audiobook.controllers.api;

import com.lydbook.audiobook.dao.DAOPassword;
import com.lydbook.audiobook.entity.User;
import com.lydbook.audiobook.services.UserService;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;


@RestController
@RequestMapping("api/v1/user")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping
    public void createUser(@RequestBody User user) {
        userService.createUser(user);
    }

    @DeleteMapping("{id}/")
    public void createUser(@PathVariable(name = "id") Long id) {
        userService.deleteUser(id);
    }

    @GetMapping("{id}/")
    public User getUser(@PathVariable("id") Long id) {
        return userService.getUser(id);
    }

    @PutMapping
    public void updateUser(@RequestBody User user) {
        userService.updateUser(user);
    }

    @PutMapping("password")
    public boolean updatePassword(@RequestBody DAOPassword password, @AuthenticationPrincipal Principal principal){
        return userService.updatePassword(principal.getName(), password);
    }
}
