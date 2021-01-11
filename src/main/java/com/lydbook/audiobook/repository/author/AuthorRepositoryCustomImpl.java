package com.lydbook.audiobook.repository.author;

import com.lydbook.audiobook.config.IAuthenticationFacade;
import com.lydbook.audiobook.entity.Author;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.List;

/**
 * Custom Author Repository
 */
@Repository
public class AuthorRepositoryCustomImpl implements AuthorRepositoryCustom {
    private final EntityManager entityManager;
    private final IAuthenticationFacade authenticationFacade;
    /**
     * Instantiates a new Author repository custom.
     *
     * @param entityManager the entity manager
     * @param authenticationFacade
     */
    public AuthorRepositoryCustomImpl(EntityManager entityManager, IAuthenticationFacade authenticationFacade) {
        this.entityManager = entityManager;
        this.authenticationFacade = authenticationFacade;
    }


    /**
     * Finds (or create if author doesn't already exist) author by name
     *
     * @param name author name
     * @return the Author
     */
    @Override
    public Author findOrCreateAuthorByName(String name) {
        CriteriaBuilder cBuild = entityManager.getCriteriaBuilder();
        CriteriaQuery<Author> criteriaQ = cBuild.createQuery(Author.class);

        Root<Author> authorRoot = criteriaQ.from(Author.class);
        Predicate namePredicate = cBuild.equal(authorRoot.get("name"), name);
        criteriaQ.where(namePredicate);

        List<Author> resultList = entityManager
                .createQuery(criteriaQ)
                .setFirstResult(0)
                .setMaxResults(1)
                .getResultList();

        if (!resultList.isEmpty()) {
            return resultList.get(0);
        }

        Author author = new Author();
        author.setName(name);
        entityManager.persist(author);

        return author;
    }
}
