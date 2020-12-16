package com.lydbook.audiobook.controllers;

import com.lydbook.audiobook.entity.Book;
import com.lydbook.audiobook.entity.Progress;
import com.lydbook.audiobook.entity.User;
import com.lydbook.audiobook.repository.AuthorRepository;
import com.lydbook.audiobook.repository.BookRepository;
import com.lydbook.audiobook.repository.ProgressRepository;
import com.lydbook.audiobook.repository.UserRepository;
import com.lydbook.audiobook.services.BookService;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.security.Principal;

@Controller
public class BookController {
    private final BookRepository bookRepository;
    private final UserRepository userRepository;
    private final ProgressRepository progressRepository;
    private final AuthorRepository authorRepository;
    private final BookService bookService;

    public BookController(BookRepository bookRepository, UserRepository userRepository, ProgressRepository progressRepository, AuthorRepository authorRepository, BookService bookService) {
        this.bookRepository = bookRepository;
        this.userRepository = userRepository;
        this.progressRepository = progressRepository;
        this.authorRepository = authorRepository;
        this.bookService = bookService;
    }

    @GetMapping("/book/{id}")
    public String getBook(Model model, @PathVariable("id") Long id, @AuthenticationPrincipal Principal principal) {
        Book book = bookService.getAllowedBookByIdAndUsername(id, principal.getName());
        if (book ==null){
            return "redirect:/books/";
        }
        User user = userRepository.findUserByUsername(principal.getName());
        Progress progress = progressRepository.findFirstByBookAndUser(book, user);
        model.addAttribute("book", book);
        model.addAttribute("progress", progress);
        model.addAttribute("authors", authorRepository.findAllByOrderByNameAsc());
        return "book";
    }

    @GetMapping("/books/")
    public String getBooks(Model model, @AuthenticationPrincipal Principal principal) {
        model.addAttribute("books", bookService.getAllowedBooksByUsername(principal.getName()));
        return "books";
    }

}
