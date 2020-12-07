package ch.fhnw.webec.repository;

import ch.fhnw.webec.entity.Author;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.List;

@Repository
public class AuthorRepositoryCustomImpl implements AuthorRepositoryCustom {
    private final EntityManager entityManager;

    public AuthorRepositoryCustomImpl(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

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
