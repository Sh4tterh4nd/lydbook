package com.lydbook.audiobook.controllers;

import com.lydbook.audiobook.repository.series.SeriesRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
public class SeriesController {
    private final SeriesRepository seriesRepository;

    public SeriesController(SeriesRepository seriesRepository) {
        this.seriesRepository = seriesRepository;
    }

   @GetMapping("/series/{id}")
    public String getSeries(Model model,@PathVariable("id") Long seriesId){
        model.addAttribute("series", seriesRepository.findSeriesById(seriesId));
        return "series";
    }
}
