package ch.fhnw.webec.repository;

import ch.fhnw.webec.entity.Author;
import ch.fhnw.webec.entity.Book;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BookRepository extends JpaRepository<Book, Long> {
    Book findBookById(Long id);
    List<Book> findAllByOrderByTitleAsc();
    void  deleteBookById(Long id);
}
