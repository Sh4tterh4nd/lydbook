package com.lydbook.audiobook.controllers.api;

import com.lydbook.audiobook.entity.Book;
import com.lydbook.audiobook.repository.author.AuthorRepository;
import com.lydbook.audiobook.repository.book.BookRepository;
import com.lydbook.audiobook.services.BookService;
import com.lydbook.audiobook.services.ProgressService;
import com.lydbook.audiobook.util.BookUtil;
import com.mpatric.mp3agic.InvalidDataException;
import com.mpatric.mp3agic.UnsupportedTagException;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.*;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.Principal;
import java.util.List;
import java.util.Objects;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

@RestController
@RequestMapping("api/v1/audiobooks")
public class ApiAudiobookController {
    private final BookService bookService;
    private final ProgressService progressService;
    private final AuthorRepository authorRepository;
    private final BookRepository bookRepository;

    public ApiAudiobookController(BookService bookService, ProgressService progressService, AuthorRepository authorRepository, BookRepository bookRepository) {
        this.bookService = bookService;
        this.progressService = progressService;
        this.authorRepository = authorRepository;
        this.bookRepository = bookRepository;
    }

    @PostMapping
    public Book addAudiobook(@RequestParam("data") MultipartFile multipartFile) throws IOException, InvalidDataException, UnsupportedTagException {
        String uuidFileName = UUID.randomUUID().toString();
        String newFilename = uuidFileName.concat(BookUtil.getFileEnding(multipartFile.getOriginalFilename()));
        Path upload = Paths.get("data");
        if (Files.notExists(upload)) {
            Files.createDirectory(upload);
        }
        Path output = upload.resolve(Objects.requireNonNull(newFilename));
        OutputStream outputStream = new FileOutputStream(output.toFile());
        multipartFile.getInputStream().transferTo(outputStream);
        outputStream.flush();
        outputStream.close();

        return bookService.addAudiobook(uuidFileName, output);
    }

    @GetMapping(value = "{bookId}/stream")
    public ResponseEntity<FileSystemResource> streamFile(@PathVariable("bookId") Long bookId) {
        Book book = bookRepository.findAllowedBookById(bookId);
        if (book == null) return ResponseEntity.notFound().build();

        Path bookPath = Paths.get("data", book.getDataName().concat(".mp3"));
        return ResponseEntity.ok().body(new FileSystemResource(bookPath));
    }


    @GetMapping(value = "{bookId}/cover", produces = MediaType.IMAGE_JPEG_VALUE)
    public ResponseEntity<byte[]> getCover(@PathVariable("bookId") Long bookId) throws IOException {
        HttpHeaders headers = new HttpHeaders();

        headers.setCacheControl(CacheControl.maxAge(30, TimeUnit.MINUTES));

        Book book = bookRepository.findAllowedBookById(bookId);
        if (book == null) {
            return new ResponseEntity<>(new byte[]{}, new HttpHeaders(), HttpStatus.NOT_FOUND);
        }
        byte[] image = Files.readAllBytes(Paths.get("data", book.getDataName().concat(".jpeg")));
        ResponseEntity<byte[]> responseEntity = new ResponseEntity<>(image, headers, HttpStatus.OK);
        return responseEntity;
    }

    @GetMapping(value = "{bookId}")
    public Book getBook(@PathVariable("bookId") Long bookId){
        return bookRepository.findBookById(bookId);
    }

    @PutMapping(value = "{bookId}")
    public ResponseEntity updateBook(@PathVariable("bookId") Long bookId, @RequestBody Book book) {
        if (book.getId().equals(bookId)) {
            bookService.updateAudiobookAndTags(book);
            return new ResponseEntity(HttpStatus.NO_CONTENT);
        } else {
            return new ResponseEntity(HttpStatus.NOT_ACCEPTABLE);
        }

    }

    @DeleteMapping(value = "{bookId}")
    public void deleteBook(@PathVariable("bookId") Long bookId) {
        bookService.deleteAudiobook(bookId);
    }

    @GetMapping(value = "")
    public List<Book> getBooks(){
        return bookRepository.findAllowedBooks();
    }
}
