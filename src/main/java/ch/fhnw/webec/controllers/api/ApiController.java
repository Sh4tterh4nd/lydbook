package ch.fhnw.webec.controllers.api;

import ch.fhnw.webec.entity.Author;
import ch.fhnw.webec.entity.Book;
import ch.fhnw.webec.entity.Tag;
import ch.fhnw.webec.repository.AuthorRepository;
import ch.fhnw.webec.repository.BookRepository;
import ch.fhnw.webec.services.ImportAudiobookService;
import ch.fhnw.webec.services.ProgressService;
import ch.fhnw.webec.util.AudiobookUtil;
import com.mpatric.mp3agic.InvalidDataException;
import com.mpatric.mp3agic.UnsupportedTagException;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.Principal;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

@RestController
@RequestMapping("api/v1/audiobook")
public class ApiController {
    private final ImportAudiobookService importAudiobookService;
    private final ProgressService progressService;
    private final AuthorRepository authorRepository;
    private final BookRepository bookRepository;

    public ApiController(ImportAudiobookService importAudiobookService, ProgressService progressService, AuthorRepository authorRepository, BookRepository bookRepository) {
        this.importAudiobookService = importAudiobookService;
        this.progressService = progressService;
        this.authorRepository = authorRepository;
        this.bookRepository = bookRepository;
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

        importAudiobookService.addAudiobook(multipartFile.getOriginalFilename(), output);
    }

    @GetMapping
    public String getAudiobooks(@RequestParam("user") String str) {
        List<Author> authorsByName = authorRepository.findAuthorsByName(str);
        System.out.println(str);
        System.out.println(authorsByName.size());
        return "test";
    }

    @GetMapping(value = "{bookId}")
    public String getBook(@PathVariable("bookId") String bookId) {
        return bookId;
    }


    @GetMapping(value = "{bookId}/stream")
    public ResponseEntity<FileSystemResource> streamFile(@PathVariable("bookId") Long bookId) {
        Book book = bookRepository.findBookById(bookId);
        if (book == null) return ResponseEntity.notFound().build();

        Path bookPath = Paths.get("data", book.getFilename());
        return ResponseEntity.ok().body(new FileSystemResource(bookPath));
    }

    @DeleteMapping("{bookId}")
    public String deleteBook() {
        return "deleted";
    }

    @PostMapping("{bookId}/progress")
    public String updateProgress(@PathVariable("bookId") Long bookId, @RequestBody String body, @AuthenticationPrincipal Principal principal) {
        if (AudiobookUtil.isNumeric(body)) {
            progressService.updateProgress(principal.getName(), bookId, (int) Double.parseDouble(body));
        }
        return "";
    }

    @GetMapping("/tag")
    public List<Tag> getTags() {
        List<Tag> tags = new ArrayList<>();
        tags.add(new Tag("Test", 1L));
        tags.add(new Tag("Haus", 2L));
        tags.add(new Tag("Brot", 3L));
        tags.add(new Tag("Auto", 4L));
        tags.add(new Tag("Pferd", 4L, false));
        tags.add(new Tag("Wetter", 4L, false));

        return tags;
    }
}
