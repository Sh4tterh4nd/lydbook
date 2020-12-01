package ch.fhnw.webec.repository;

import ch.fhnw.webec.entity.Book;
import ch.fhnw.webec.entity.Progress;
import ch.fhnw.webec.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProgressRepository extends JpaRepository<Progress,Long> {

    List<Progress> findProgressesByUser(User user);
    List<Progress> findProgressByBookAndUser(Book book, User user);

}
