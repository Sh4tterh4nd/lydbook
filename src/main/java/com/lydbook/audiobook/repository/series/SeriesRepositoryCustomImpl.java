package com.lydbook.audiobook.repository.series;

import com.lydbook.audiobook.entity.Author;
import com.lydbook.audiobook.entity.Series;
import com.lydbook.audiobook.entity.Tag;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.criteria.*;
import javax.transaction.Transactional;
import java.util.List;
@Repository
public class SeriesRepositoryCustomImpl  implements SeriesRepositoryCustom {
    private final EntityManager entityManager;

    public SeriesRepositoryCustomImpl(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Override
    @Transactional
    public void removeAllUnusedSeries() {
        CriteriaBuilder cBuild = entityManager.getCriteriaBuilder();
        CriteriaDelete<Series> deleteQuery = cBuild.createCriteriaDelete(Series.class);

        Root<Series> seriesRoot = deleteQuery.from(Series.class);
        Predicate namePredicate = cBuild.isEmpty(seriesRoot.get("books"));

        deleteQuery.where(namePredicate);
        entityManager
                .createQuery(deleteQuery)
                .executeUpdate();
    }

    @Override
    public List<Series> findAllSeriesByAuthor(Author author) {
        return null;
    }

    @Override
    public List<Series> findAllSeriesByAuthor(Long authorId) {
        return null;
    }

    @Override
    @Transactional
    public Series findOrCreateSeries(String seriesName) {
        CriteriaBuilder cBuild = entityManager.getCriteriaBuilder();
        CriteriaQuery<Series> selectQuery = cBuild.createQuery(Series.class);

        Root<Series> seriesRoot = selectQuery.from(Series.class);
        Predicate namePredicate = cBuild.equal(seriesRoot.get("name"), seriesName);
        selectQuery.where(namePredicate);

        List<Series> resultList = entityManager
                .createQuery(selectQuery)
                .setFirstResult(0)
                .setMaxResults(1)
                .getResultList();

        if (!resultList.isEmpty()) {
            return resultList.get(0);
        }
        Series newSeries = new Series(seriesName);
        entityManager.persist(newSeries);

        return newSeries;
    }
}
