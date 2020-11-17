package ch.fhnw.webec.controllers;

import ch.fhnw.webec.services.ImportAudiobookService;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.File;

@RestController
@RequestMapping("api/v1/audiobook")
public class ApiController {
    private final ImportAudiobookService importAudiobookService;

    public ApiController(ImportAudiobookService importAudiobookService) {
        this.importAudiobookService = importAudiobookService;
    }

    @PostMapping
    public String addAudiobook() {
        return "";
    }

    @GetMapping
    public String getAudiobooks() {
        return "test";
    }

    @GetMapping(value = "{bookId}")
    public String getBook(@PathVariable("bookId") String bookId) {

        return bookId;
    }

    @GetMapping(value = "{bookId}/stream")
    public ResponseEntity<FileSystemResource> getFile(@PathVariable("bookId") String bookId) {
        return ResponseEntity.ok().body(new FileSystemResource(new File("B:\\a.mp3")));
    }


    @DeleteMapping("{bookId}")
    public String deleteBook() {
        return "deleted";
    }
}
