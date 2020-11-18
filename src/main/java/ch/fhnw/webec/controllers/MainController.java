package ch.fhnw.webec.controllers;

import ch.fhnw.webec.repository.AuthorRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class MainController {
    private final AuthorRepository authorRepository;

    public MainController(AuthorRepository authorRepository) {
        this.authorRepository = authorRepository;
    }

    @GetMapping("/")
    public String index(){
        return "index";
    }

    @GetMapping("/authors/")
    public String authors(Model model){
        model.addAttribute("authors", authorRepository.findAllByOrderByNameAsc());
        return "authors";
    }
}
