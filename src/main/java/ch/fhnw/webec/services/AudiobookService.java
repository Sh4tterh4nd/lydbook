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

import javax.transaction.Transactional;
import java.io.IOException;
import java.nio.file.Path;
import java.util.Iterator;

@Service
public class AudiobookService {
    private final AuthorRepository authorRepository;
    private final BookRepository bookRepository;
    private final TagRepository tagRepository;

    public AudiobookService(AuthorRepository authorRepository, BookRepository bookRepository, TagRepository tagRepository) {
        this.authorRepository = authorRepository;
        this.bookRepository = bookRepository;
        this.tagRepository = tagRepository;
    }

    public void addAudiobook(String originalFilename, Path file) throws InvalidDataException, IOException, UnsupportedTagException {
        Book book = new Book();
        Mp3File mp3file = new Mp3File(file);
        String authorName = "";
        System.out.println(mp3file.getLengthInMilliseconds());
        System.out.println(mp3file.getLengthInSeconds());
        System.out.println(mp3file.getLength());
        System.out.println(mp3file.isVbr());
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
//        System.out.println(mp3file.isVbr());


        Author a = authorRepository.findOrCreateAuthorByName(authorName);
        Tag authorTag = tagRepository.findOrCreateFirstByName(a.getName(), false);
        Tag audiobookTag = tagRepository.findOrCreateFirstByName("Audiobook", false);

        book.addTag(authorTag);
        book.addTag(audiobookTag);
        book.setAuthor(a);
        bookRepository.save(book);
    }

    @Transactional
    public void updateAudiobook(Book updatedBook) {
        Book oldBook = bookRepository.findBookById(updatedBook.getId());
        oldBook.setTitle(updatedBook.getTitle());
        if (!oldBook.getAuthor().equals(updatedBook.getAuthor())) {
            Iterator<Tag> iterator = oldBook.getTags().iterator();
            while (iterator.hasNext()) {
                Tag t = iterator.next();
                if (t.getName().equals(oldBook.getAuthor().getName())) {
                    iterator.remove();
                }
            }
            oldBook.setAuthor(authorRepository.findOrCreateAuthorByName(updatedBook.getAuthor().getName()));
            oldBook.addTag(tagRepository.findOrCreateFirstByName(oldBook.getAuthor().getName()));
        }
        //remove Tags that are not existing anymore
        Iterator<Tag> iterator = oldBook.getTags().iterator();
        while (iterator.hasNext()) {
            Tag tag = iterator.next();
            if (!updatedBook.getTags().contains(tag) && tag.isRemovable()) {
                iterator.remove();
            }
        }

//add new tags
        iterator = updatedBook.getTags().iterator();
        while (iterator.hasNext()) {
            Tag tag = iterator.next();
            if (!oldBook.getTags().contains(tag)) {
                oldBook.addTag(tagRepository.findOrCreateFirstByName(tag.getName()));
            }
        }
        bookRepository.save(oldBook);

    }
}
