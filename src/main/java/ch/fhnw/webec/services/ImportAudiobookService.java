package ch.fhnw.webec.services;

import ch.fhnw.webec.entity.Author;
import ch.fhnw.webec.entity.Book;
import ch.fhnw.webec.entity.Tag;
import ch.fhnw.webec.repository.AuthorRepository;
import ch.fhnw.webec.repository.BookRepository;
import ch.fhnw.webec.repository.TagRepository;
import com.mpatric.mp3agic.ID3v1;
import com.mpatric.mp3agic.InvalidDataException;
import com.mpatric.mp3agic.Mp3File;
import com.mpatric.mp3agic.UnsupportedTagException;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.Path;

@Service
public class ImportAudiobookService {
    private final AuthorRepository authorRepository;
    private final BookRepository bookRepository;
    private final TagRepository tagRepository;

    public ImportAudiobookService(AuthorRepository authorRepository, BookRepository bookRepository, TagRepository tagRepository) {
        this.authorRepository = authorRepository;
        this.bookRepository = bookRepository;
        this.tagRepository = tagRepository;
    }

    public void addAudiobook(String originalFilename, Path file) throws InvalidDataException, IOException, UnsupportedTagException {
        Book book = new Book();
        Mp3File mp3file = new Mp3File(file);
        String authorName = "";

        book.setLength(mp3file.getLengthInSeconds());
        book.setFilename(file.getFileName().toString());
        if (mp3file.hasId3v1Tag()) {
            ID3v1 id3v1Tag = mp3file.getId3v1Tag();
            book.setTitle(id3v1Tag.getAlbum());
            authorName = id3v1Tag.getArtist();
        }
        if (mp3file.hasId3v2Tag()) {
            ID3v1 id3v2Tag = mp3file.getId3v2Tag();
            book.setTitle(id3v2Tag.getAlbum());
            authorName = id3v2Tag.getArtist();
        }
        System.out.println(mp3file.isVbr());


        Author a = authorRepository.findOrCreateAuthorByName(authorName);
        Tag authorTag = tagRepository.findOrCreateFirstByName(a.getName());
        Tag audiobookTag = tagRepository.findOrCreateFirstByName("Audiobook");


        book.addTag(authorTag);
        book.addTag(audiobookTag);
        book.setAuthor(a);
        bookRepository.save(book);
    }
}
