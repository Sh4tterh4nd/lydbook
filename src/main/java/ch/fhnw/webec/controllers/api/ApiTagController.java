package ch.fhnw.webec.controllers.api;

import ch.fhnw.webec.entity.Tag;
import ch.fhnw.webec.repository.TagRepository;
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
        return tagRepository.findAllByOrderByName();
    }

}
