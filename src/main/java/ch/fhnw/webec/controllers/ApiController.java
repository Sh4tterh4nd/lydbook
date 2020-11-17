package ch.fhnw.webec.controllers;

import ch.fhnw.webec.services.ImportAudiobookService;
import ch.fhnw.webec.util.AudiobookUtil;
import com.mpatric.mp3agic.InvalidDataException;
import com.mpatric.mp3agic.UnsupportedTagException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Objects;
import java.util.UUID;

@RestController
@RequestMapping("api/v1/audiobook")
public class ApiController {
    private final ImportAudiobookService importAudiobookService;

    public ApiController(ImportAudiobookService importAudiobookService) {
        this.importAudiobookService = importAudiobookService;
    }

    @PostMapping
    public void addAudiobook(@RequestParam("data") MultipartFile multipartFile) throws IOException, InvalidDataException, UnsupportedTagException {
        String newFilename = UUID.randomUUID().toString().concat(AudiobookUtil.getFileEnding(multipartFile.getOriginalFilename()));
        Path upload = Paths.get("data");
        if (Files.notExists(upload)) {
            Files.createDirectory(upload);
        }
        Path output = upload.resolve(Objects.requireNonNull(newFilename));
        OutputStream outputStream = new FileOutputStream(output.toFile());
        multipartFile.getInputStream().transferTo(outputStream);
        outputStream.flush();
        outputStream.close();

        importAudiobookService.addAudiobook(multipartFile.getOriginalFilename(),output);
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
