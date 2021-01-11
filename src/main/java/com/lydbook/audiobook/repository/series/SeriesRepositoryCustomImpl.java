package com.lydbook.audiobook.repository.series;

import com.lydbook.audiobook.config.IAuthenticationFacade;
import com.lydbook.audiobook.entity.Author;
import com.lydbook.audiobook.entity.Book;
import com.lydbook.audiobook.entity.Series;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.*;
import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

@Repository
public class SeriesRepositoryCustomImpl implements SeriesRepositoryCustom {
    private final EntityManager entityManager;

    private final IAuthenticationFacade authenticationFacade;

    public SeriesRepositoryCustomImpl(EntityManager entityManager, IAuthenticationFacade authenticationFacade) {
        this.entityManager = entityManager;
        this.authenticationFacade = authenticationFacade;
    }

    private String getLoggedinUser() {
        return authenticationFacade.getAuthentication().getName();
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
    public List<Series> findAllSeries() {
        TypedQuery<Series> q = entityManager.createQuery("select distinct series from User user " +
                "join user.tags tags " +
                "join tags.books book " +
                "join book.series series " +
                "where user.username= :username", Series.class);
        q.setParameter("username", getLoggedinUser());
        List<Series> resultQuery = q.getResultList();
        if (resultQuery.isEmpty()) return resultQuery;

        List<Series> resultList = new ArrayList<>();
        for (Series series : resultQuery) {
            resultList.add(findSeriesById(series.getId()));
        }
        return resultList;
    }


    @Override
    public List<Series> findAllSeriesByAuthor(Long authorId) {
        TypedQuery<Series> q = entityManager.createQuery("select distinct series from User user " +
                "join user.tags tags " +
                "join tags.books book " +
                "join book.series series " +
                "where user.username= :username and book.author.id = :authroId", Series.class);
        q.setParameter("username", getLoggedinUser());
        q.setParameter("authroId", authorId);


        List<Series> resultList = new ArrayList<>();
        for (Series series : q.getResultList()) {
            resultList.add(findSeriesById(series.getId()));
        }

        return resultList;
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

    @Override
    public Series findSeriesById(Long seriesId) {
        TypedQuery<Series> q = entityManager.createQuery("select series from User user " +
                "join user.tags tags " +
                "join tags.books book " +
                "join book.series series " +
                "where user.username= :username and series.id = :seriesId", Series.class);

        q.setParameter("username", getLoggedinUser());
        q.setParameter("seriesId", seriesId);
        q.setMaxResults(1);
        List<Series> resultList = q.getResultList();
        if (resultList.isEmpty()) {
            return null;
        }
        Series series = resultList.get(0);
        series.setBooks(findBooksForSeries(seriesId));


        return series;
    }

    public List<Book> findBooksForSeries(Long seriesId) {
        TypedQuery<Book> q = entityManager.createQuery("select book from User user " +
                "join user.tags tags " +
                "join tags.books book " +
                "join book.series series " +
                "where user.username= :username and series.id = :seriesId order by book.bookNumber asc", Book.class);


        q.setParameter("username", getLoggedinUser());
        q.setParameter("seriesId", seriesId);

        return q.getResultList();
    }
}
