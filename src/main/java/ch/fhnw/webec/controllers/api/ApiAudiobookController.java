package ch.fhnw.webec.controllers.api;

import ch.fhnw.webec.entity.Book;
import ch.fhnw.webec.repository.AuthorRepository;
import ch.fhnw.webec.repository.BookRepository;
import ch.fhnw.webec.services.AudiobookService;
import ch.fhnw.webec.services.ProgressService;
import ch.fhnw.webec.util.AudiobookUtil;
import com.mpatric.mp3agic.InvalidDataException;
import com.mpatric.mp3agic.UnsupportedTagException;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.Principal;
import java.util.Objects;
import java.util.UUID;

@RestController
@RequestMapping("api/v1/audiobook")
public class ApiAudiobookController {
    private final AudiobookService audiobookService;
    private final ProgressService progressService;
    private final AuthorRepository authorRepository;
    private final BookRepository bookRepository;

    public ApiAudiobookController(AudiobookService audiobookService, ProgressService progressService, AuthorRepository authorRepository, BookRepository bookRepository) {
        this.audiobookService = audiobookService;
        this.progressService = progressService;
        this.authorRepository = authorRepository;
        this.bookRepository = bookRepository;
    }

    @PostMapping
    public Book addAudiobook(@RequestParam("data") MultipartFile multipartFile) throws IOException, InvalidDataException, UnsupportedTagException {
        String uuidFileName = UUID.randomUUID().toString();
        String newFilename = uuidFileName.concat(AudiobookUtil.getFileEnding(multipartFile.getOriginalFilename()));
        Path upload = Paths.get("data");
        if (Files.notExists(upload)) {
            Files.createDirectory(upload);
        }
        Path output = upload.resolve(Objects.requireNonNull(newFilename));
        OutputStream outputStream = new FileOutputStream(output.toFile());
        multipartFile.getInputStream().transferTo(outputStream);
        outputStream.flush();
        outputStream.close();

        return audiobookService.addAudiobook(uuidFileName, output);
    }

    @GetMapping(value = "{bookId}/stream")
    public ResponseEntity<FileSystemResource> streamFile(@PathVariable("bookId") Long bookId, @AuthenticationPrincipal Principal principal) {
        Book book = audiobookService.findAllowedBookByIdAndUsername(bookId, principal.getName());
        if (book == null) return ResponseEntity.notFound().build();

        Path bookPath = Paths.get("data", book.getDataName().concat(".mp3"));
        return ResponseEntity.ok().body(new FileSystemResource(bookPath));
    }

    @GetMapping(value = "{bookId}/cover", produces = MediaType.IMAGE_JPEG_VALUE)
    public @ResponseBody
    byte[] getCover(@PathVariable("bookId") Long bookId, @AuthenticationPrincipal Principal principal) throws IOException {
        Book book = audiobookService.findAllowedBookByIdAndUsername(bookId, principal.getName());
        if (book == null) {
            return new byte[0];
        }
        return Files.readAllBytes(Paths.get("data", book.getDataName().concat(".jpeg")));
    }


    @PutMapping("{bookId}/")
    public ResponseEntity updateBook(@PathVariable("bookId") Long bookId, @RequestBody Book book) {
        if (book.getId().equals(bookId)) {
            audiobookService.updateAudiobookAndTags(book);
            return new ResponseEntity(HttpStatus.NO_CONTENT);
        } else {
            return new ResponseEntity(HttpStatus.NOT_ACCEPTABLE);
        }

    }

    @DeleteMapping("{bookId}/")
    public void deleteBook(@PathVariable("bookId") Long bookId) {
        audiobookService.deleteAudiobook(bookId);
    }
}
