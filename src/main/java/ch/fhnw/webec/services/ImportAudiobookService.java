package ch.fhnw.webec.services;

import ch.fhnw.webec.repository.AuthorRepository;
import ch.fhnw.webec.repository.BookRepository;
import org.springframework.stereotype.Service;

@Service
public class ImportAudiobookService {
    private final AuthorRepository authorRepository;
    private final BookRepository bookRepository;

    public ImportAudiobookService(AuthorRepository authorRepository, BookRepository bookRepository) {
        this.authorRepository = authorRepository;
        this.bookRepository = bookRepository;
    }

    public void addAudiobook(){

    }
}
