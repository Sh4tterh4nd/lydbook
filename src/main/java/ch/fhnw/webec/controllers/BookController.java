package ch.fhnw.webec.controllers;

import ch.fhnw.webec.entity.Book;
import ch.fhnw.webec.entity.Progress;
import ch.fhnw.webec.entity.User;
import ch.fhnw.webec.repository.BookRepository;
import ch.fhnw.webec.repository.ProgressRepository;
import ch.fhnw.webec.repository.UserRepository;
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

    public BookController(BookRepository bookRepository, UserRepository userRepository, ProgressRepository progressRepository) {
        this.bookRepository = bookRepository;
        this.userRepository = userRepository;
        this.progressRepository = progressRepository;
    }

    @GetMapping("/book/{id}")
    public String getBook(Model model, @PathVariable("id") Long id, @AuthenticationPrincipal Principal principal) {
        Book book = bookRepository.findBookById(id);
        User user = userRepository.findUserByUsername(principal.getName());
        Progress progress = progressRepository.findFirstByBookAndUser(book,user);
        model.addAttribute("book", book);
        model.addAttribute("progress", progress);
        return "book";
    }

    @GetMapping("/books/")
    public String getBooks(Model model) {
        model.addAttribute("books", bookRepository.findAllByOrderByTitleAsc());
        return "books";
    }

}
