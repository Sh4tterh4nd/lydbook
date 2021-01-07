package com.lydbook.audiobook.controllers.api;

import com.lydbook.audiobook.entity.Tag;
import com.lydbook.audiobook.repository.tag.TagRepository;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("api/v1/tags")
public class ApiTagController {

    private final TagRepository tagRepository;

    public ApiTagController(TagRepository tagRepository) {
        this.tagRepository = tagRepository;
    }

    @GetMapping
    public List<Tag> getAllTags() {
        List<Tag> tags = tagRepository.findAllByOrderByName();
        return tags;
    }

}
