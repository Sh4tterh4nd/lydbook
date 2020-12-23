package com.lydbook.audiobook.repository.series;

import com.lydbook.audiobook.entity.Series;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
public interface SeriesRepository  extends JpaRepository<Series,Long>, SeriesRepositoryCustom{
    List<Series> findAllByOrderByName();
}
