package com.lydbook.audiobook.repository.book;

import com.lydbook.audiobook.entity.Book;
import java.util.List;

public interface BookRepositoryCustom {
    void deleteBookById(Long id);
    List<Book> findAllowedBooks();
    List<Book> findAllowedBooksByUsername(String username);
    Book findAllowedBookById(Long bookId);
    Book findAllowedBookById(Long bookId, String username);
}
