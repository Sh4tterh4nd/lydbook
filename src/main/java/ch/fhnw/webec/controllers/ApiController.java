package ch.fhnw.webec.controllers;

import org.springframework.core.io.FileSystemResource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.File;

@RestController
@RequestMapping("api/v1/audiobook")
public class ApiController {

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
        return ResponseEntity.ok().body(new FileSystemResource(new File("B:\\abc.mp3")));
    }


    @DeleteMapping("{bookId}")
    public String deleteBook() {
        return "deleted";
    }
}
