package ch.fhnw.webec.controllers;

import ch.fhnw.webec.services.AuthorService;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.security.Principal;

@Controller
public class AuthorController {
    private final AuthorService authorService;

    public AuthorController(AuthorService authorService) {
        this.authorService = authorService;
    }

    @GetMapping("/authors/")
    public String authors(Model model, @AuthenticationPrincipal Principal principal) {
        model.addAttribute("authors", authorService.getAllowedAuthorsByUsername(principal.getName()));
        return "authors";
    }

    @GetMapping("/authors/{id}")
    public String author(Model model, @PathVariable("id") Long id, @AuthenticationPrincipal Principal principal) {
        model.addAttribute("author", authorService.getAllowedAuthorByIdAndUsername(id, principal.getName()));
        return "author";
    }
}
