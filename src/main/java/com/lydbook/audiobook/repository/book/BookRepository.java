package com.lydbook.audiobook.repository.book;

import com.lydbook.audiobook.entity.Book;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BookRepository extends JpaRepository<Book, Long>,BookRepositoryCustom {
    Book findBookById(Long id);
}
