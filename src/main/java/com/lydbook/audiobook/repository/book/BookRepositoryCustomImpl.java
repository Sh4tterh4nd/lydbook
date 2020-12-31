package com.lydbook.audiobook.repository.book;

import com.lydbook.audiobook.config.IAuthenticationFacade;
import com.lydbook.audiobook.entity.Book;
import com.lydbook.audiobook.entity.Progress;
import com.lydbook.audiobook.entity.User;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaDelete;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.transaction.Transactional;
import java.util.List;

/**
 * Custom Book Repository
 */
@Repository
public class BookRepositoryCustomImpl implements BookRepositoryCustom {

    private final EntityManager entityManager;
    private final IAuthenticationFacade authenticationFacade;

    /**
     * Instantiates a new Book repository custom.
     *
     * @param entityManager        the entity manager
     * @param authenticationFacade
     */
    public BookRepositoryCustomImpl(EntityManager entityManager, IAuthenticationFacade authenticationFacade) {
        this.entityManager = entityManager;
        this.authenticationFacade = authenticationFacade;
    }

    private String getLoggedinUser() {
        return authenticationFacade.getAuthentication().getName();
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
        Predicate namePredicate = cBuild.equal(tag.get("id"), id);

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
    public void deleteProgressForBookById(Long id) {
        CriteriaBuilder cBuild = entityManager.getCriteriaBuilder();
        CriteriaDelete<Progress> deleteQuery = cBuild.createCriteriaDelete(Progress.class);

        Root<Progress> tag = deleteQuery.from(Progress.class);
        Predicate namePredicate = cBuild.equal(tag.get("book"), id);

        deleteQuery.where(namePredicate);
        entityManager
                .createQuery(deleteQuery)
                .executeUpdate();
    }

    @Override
    public List<Book> findAllowedBooks() {
        return findAllowedBooksByUsername(getLoggedinUser());
    }

    @Override
    public List<Book> findAllowedBooksByUsername(String username) {
        TypedQuery<Book> bookQuery = entityManager.createQuery("select books from User user " +
                "join user.tags tags " +
                "join tags.books books " +
                "where user.username = :username " +
                "order by books.title", Book.class);

        bookQuery.setParameter("username", username);

        return bookQuery.getResultList();
    }
    @Override
    public Book findAllowedBookById(Long bookId) {
        return findAllowedBookById(bookId, getLoggedinUser());
    }

    @Override
    public Book findAllowedBookById(Long bookId, String username) {
        TypedQuery<Book> bookQuery = entityManager.createQuery("select books from User user " +
                "join user.tags tags " +
                "join tags.books books " +
                "where user.username = :username " +
                "and books.id = :bookid", Book.class);

        bookQuery.setParameter("username", username);
        bookQuery.setParameter("bookid", bookId);
        bookQuery.setMaxResults(1);
        List<Book> resultList = bookQuery.getResultList();
        if (resultList.isEmpty()){
            return null;
        }
        return resultList.get(0);
    }
}
