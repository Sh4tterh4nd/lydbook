package ch.fhnw.webec.repository;

import ch.fhnw.webec.entity.Book;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BookRepository extends JpaRepository<Book, Long> {
    Book findBookById(Long id);

    void deleteBookById(Long id);
}
