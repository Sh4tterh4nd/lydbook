package ch.fhnw.webec.repository;

import ch.fhnw.webec.entity.Author;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AuthorRepository extends JpaRepository<Author,Long>, AuthorRepositoryCustom {
    List<Author> findAuthorsByName(String name);
    Author findAuthorByName(String name);
    Author findAuthorById(Long id);
    List<Author> findAllByOrderByNameAsc();
//
}
