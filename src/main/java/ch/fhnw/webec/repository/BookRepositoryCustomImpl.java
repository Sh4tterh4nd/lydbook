package ch.fhnw.webec.repository;

import ch.fhnw.webec.entity.Book;
import ch.fhnw.webec.entity.Progress;
import ch.fhnw.webec.entity.Tag;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaDelete;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.transaction.Transactional;

@Repository
public class BookRepositoryCustomImpl implements BookRepositoryCustom {

    private final EntityManager entityManager;

    public BookRepositoryCustomImpl(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Override
    public void deleteBookById(Long id) {
        deleteProgressForBookById(id);
        CriteriaBuilder cBuild = entityManager.getCriteriaBuilder();
        CriteriaDelete<Book> deleteQuery = cBuild.createCriteriaDelete(Book.class);

        Root<Book> tag = deleteQuery.from(Book.class);
        Predicate namePredicate = cBuild.equal(tag.get("id"),id);

        deleteQuery.where(namePredicate);
        entityManager
                .createQuery(deleteQuery)
                .executeUpdate();

    }

    public void deleteProgressForBookById(Long id){
        CriteriaBuilder cBuild = entityManager.getCriteriaBuilder();
        CriteriaDelete<Progress> deleteQuery = cBuild.createCriteriaDelete(Progress.class);

        Root<Progress> tag = deleteQuery.from(Progress.class);
        Predicate namePredicate = cBuild.equal(tag.get("book"),id);

        deleteQuery.where(namePredicate);
        entityManager
                .createQuery(deleteQuery)
                .executeUpdate();
    }
}
