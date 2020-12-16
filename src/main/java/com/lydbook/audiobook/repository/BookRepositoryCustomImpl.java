package com.lydbook.audiobook.repository;

import com.lydbook.audiobook.entity.Book;
import com.lydbook.audiobook.entity.Progress;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaDelete;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.transaction.Transactional;

/**
 * Custom Book Repository
 */
@Repository
public class BookRepositoryCustomImpl implements BookRepositoryCustom {

    private final EntityManager entityManager;

    /**
     * Instantiates a new Book repository custom.
     *
     * @param entityManager the entity manager
     */
    public BookRepositoryCustomImpl(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    /**
     * Delete book by id
     *
     * @param id the book id
     */
    @Override
    @Transactional
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

    /**
     * Delete progresses that are associated with a certain book id
     *
     * @param id the id
     */
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
