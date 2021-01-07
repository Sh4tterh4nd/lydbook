package com.lydbook.audiobook.controllers;

import com.lydbook.audiobook.entity.Author;
import com.lydbook.audiobook.repository.series.SeriesRepository;
import com.lydbook.audiobook.services.AuthorService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
public class AuthorController {
    private final AuthorService authorService;
    private final SeriesRepository seriesRepository;

    public AuthorController(AuthorService authorService, SeriesRepository seriesRepository) {
        this.authorService = authorService;
        this.seriesRepository = seriesRepository;
    }

    @GetMapping("/authors/")
    public String authors(Model model) {
        model.addAttribute("authors", authorService.getAllowedAuthorsByUsername());
        return "authors";
    }

    @GetMapping("/authors/{id}")
    public String author(Model model, @PathVariable("id") Long id) {
        Author author = authorService.getAllowedAuthorByIdAndUsername(id);
        if (author == null){
            return "redirect:/authors/";
        }
        model.addAttribute("series", seriesRepository.findAllSeriesByAuthor(id));
        model.addAttribute("author", author);
        return "author";
    }
}
