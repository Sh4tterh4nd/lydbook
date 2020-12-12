package ch.fhnw.webec.repository;
import ch.fhnw.webec.entity.Tag;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TagRepository extends JpaRepository<Tag, Long>, TagRepositoryCustom {
    List<Tag> findAllByOrderByName();
    Tag findFirstByName(String name);
}
