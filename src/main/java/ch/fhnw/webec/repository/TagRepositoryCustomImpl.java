package ch.fhnw.webec.repository;

import ch.fhnw.webec.entity.Tag;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.criteria.*;
import javax.transaction.Transactional;
import java.util.List;

@Repository
public class TagRepositoryCustomImpl implements TagRepositoryCustom {

    private final EntityManager entityManager;

    public TagRepositoryCustomImpl(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Override
    public Tag findOrCreateFirstByName(String name) {
        return findOrCreateFirstByName(name, true);
    }

    @Override
    @Transactional
    public Tag findOrCreateFirstByName(String name, boolean isRemovable) {
        CriteriaBuilder cBuild = entityManager.getCriteriaBuilder();
        CriteriaQuery<Tag> selectQuery = cBuild.createQuery(Tag.class);

        Root<Tag> tag = selectQuery.from(Tag.class);
        Predicate namePredicate = cBuild.equal(tag.get("name"), name);
        selectQuery.where(namePredicate);

        List<Tag> resultList = entityManager
                .createQuery(selectQuery)
                .setFirstResult(0)
                .setMaxResults(1)
                .getResultList();

        if (!resultList.isEmpty()) {
            return resultList.get(0);
        }
        Tag newTag = new Tag(name);
        newTag.setRemovable(isRemovable);
        entityManager.persist(newTag);

        return newTag;
    }

    @Override
    @Transactional
    public void removeTagsWithNoBooks() {
        CriteriaBuilder cBuild = entityManager.getCriteriaBuilder();
        CriteriaDelete<Tag> deleteQuery = cBuild.createCriteriaDelete(Tag.class);

        Root<Tag> tag = deleteQuery.from(Tag.class);
        Predicate namePredicate = cBuild.isEmpty(tag.get("books"));

        deleteQuery.where(namePredicate);
        entityManager
                .createQuery(deleteQuery)
                .executeUpdate();
    }

    public List<Tag> findTagsWithNoBooks() {
        CriteriaBuilder cBuild = entityManager.getCriteriaBuilder();
        CriteriaQuery<Tag> criteriaQ = cBuild.createQuery(Tag.class);

        Root<Tag> tag = criteriaQ.from(Tag.class);
        Predicate namePredicate = cBuild.isEmpty(tag.get("books"));

        criteriaQ.where(namePredicate);
        return entityManager
                .createQuery(criteriaQ)
                .getResultList();
    }
}
