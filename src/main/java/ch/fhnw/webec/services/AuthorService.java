package ch.fhnw.webec.services;

import ch.fhnw.webec.entity.Author;
import ch.fhnw.webec.entity.Book;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class AuthorService {
    private final AudiobookService audiobookService;

    public AuthorService(AudiobookService audiobookService) {
        this.audiobookService = audiobookService;
    }


    public List<Author> getAllowedAuthorsByUsername(String username) {
        List<Book> books = audiobookService.getAllowedBooksByUsername(username);
        Set<Author> authorSet = new HashSet<>();
        books.forEach(book -> authorSet.add(book.getAuthor()));
        List<Author> authorList = new ArrayList<>(authorSet);
        authorList.sort(Comparator.comparing(Author::getName));
        return authorList;
    }

    public Author getAllowedAuthorByIdAndUsername(Long authorId, String username) {
        List<Book> allowedBooksByUsername = audiobookService.getAllowedBooksByUsername(username);
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
