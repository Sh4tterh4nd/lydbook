package ch.fhnw.webec.repository;

import ch.fhnw.webec.entity.Tag;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
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
        CriteriaQuery<Tag> criteriaQ = cBuild.createQuery(Tag.class);

        Root<Tag> tag = criteriaQ.from(Tag.class);
        Predicate namePredicate = cBuild.equal(tag.get("name"), name);
        criteriaQ.where(namePredicate);

        List<Tag> resultList = entityManager
                .createQuery(criteriaQ)
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
}
