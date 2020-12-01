package ch.fhnw.webec.controllers;

import ch.fhnw.webec.repository.AuthorRepository;
import ch.fhnw.webec.repository.BookRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
public class BookController {
    private final AuthorRepository authorRepository;
    private final BookRepository bookRepository;

    public BookController(AuthorRepository authorRepository, BookRepository bookRepository) {
        this.authorRepository = authorRepository;
        this.bookRepository = bookRepository;
    }

    @GetMapping("/book/{id}")
    public String getBook(Model model, @PathVariable("id") Long id){
        model.addAttribute("book", bookRepository.findBookById(id));
        model.addAttribute("progress", 600);
        return "book";
    }

    @GetMapping("/books/")
    public String getBooks(Model model){
        model.addAttribute("books", bookRepository.findAllByOrderByTitleAsc());
        return "books";
    }

}
