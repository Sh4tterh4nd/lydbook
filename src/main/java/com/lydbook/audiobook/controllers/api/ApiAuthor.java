package com.lydbook.audiobook.controllers.api;

import com.lydbook.audiobook.entity.Author;
import com.lydbook.audiobook.services.AuthorService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("api/v1/author")
public class ApiAuthor {
    private final AuthorService authorService;

    public ApiAuthor(AuthorService authorService) {
        this.authorService = authorService;
    }

    @GetMapping(value = "{authorId}")
    public Author getAuthor(@PathVariable("authorId") Long authorId) {
        return authorService.getAllowedAuthorByIdAndUsername(authorId);
    }
    @GetMapping(value ="")
    public List<Author> getAuthors(){
        return authorService.getAllowedAuthorsByUsername();
    }

}
