package com.lydbook.audiobook.services;

import com.lydbook.audiobook.entity.Author;
import com.lydbook.audiobook.entity.Book;
import com.lydbook.audiobook.repository.book.BookRepository;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * Author service.
 */
@Service
public class AuthorService {
    private final BookRepository bookRepository;

    /**
     * Instantiates a new Author service.
     *
     * @param bookRepository
     */
    public AuthorService(BookRepository bookRepository) {
        this.bookRepository = bookRepository;
    }


    /**
     * Get all Authors that own books that a user has permission to
     *
     * @return the allowed authors by username
     */
    public List<Author> getAllowedAuthorsByUsername() {
        List<Book> books = bookRepository.findAllowedBooks();
        Set<Author> authorSet = new HashSet<>();
        books.forEach(book -> authorSet.add(book.getAuthor()));
        List<Author> authorList = new ArrayList<>(authorSet);
        authorList.sort(Comparator.comparing(Author::getName));
        return authorList;
    }

    /**
     * gets Author by id if the author owns any books which the user has permission to
     *
     * @param authorId the author id
     * @return the allowed author by id and username
     */
    public Author getAllowedAuthorByIdAndUsername(Long authorId) {
        List<Book> allowedBooksByUsername = bookRepository.findAllowedBooks();
        Author author = null;
        for (Book book : allowedBooksByUsername) {
            if (book.getAuthor().getId().equals(authorId)) {
                author = book.getAuthor();
                break;
            }
        }
        if (author == null) {
            return null;
        }
        author.getBookList().clear();
        for (Book book : allowedBooksByUsername) {
            if (book.getAuthor().getId().equals(authorId)){
                author.addBook(book);
            }
        }

        return author;
    }
}
