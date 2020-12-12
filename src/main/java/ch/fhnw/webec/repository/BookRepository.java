package ch.fhnw.webec.repository;

import ch.fhnw.webec.entity.Author;
import ch.fhnw.webec.entity.Book;
import ch.fhnw.webec.entity.Tag;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Set;

@Repository
public interface BookRepository extends JpaRepository<Book, Long> {
    Book findBookById(Long id);

    void deleteBookById(Long id);
}
