package com.lydbook.audiobook.repository.series;

import com.lydbook.audiobook.entity.Author;
import com.lydbook.audiobook.entity.Series;

import javax.transaction.Transactional;
import java.util.List;

public interface SeriesRepositoryCustom {
    @Transactional
    void removeAllUnusedSeries();

    List<Series> findAllSeries();

    List<Series> findAllSeriesByAuthor(Long authorId);

    @Transactional
    Series findOrCreateSeries(String seriesName);

    Series findSeriesById(Long seriesId);
}
