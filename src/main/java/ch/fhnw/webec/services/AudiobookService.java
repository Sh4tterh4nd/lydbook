package ch.fhnw.webec.services;

import ch.fhnw.webec.entity.Author;
import ch.fhnw.webec.entity.Book;
import ch.fhnw.webec.entity.Tag;
import ch.fhnw.webec.entity.User;
import ch.fhnw.webec.repository.AuthorRepository;
import ch.fhnw.webec.repository.BookRepository;
import ch.fhnw.webec.repository.TagRepository;
import ch.fhnw.webec.repository.UserRepository;
import com.mpatric.mp3agic.ID3v1;
import com.mpatric.mp3agic.InvalidDataException;
import com.mpatric.mp3agic.Mp3File;
import com.mpatric.mp3agic.UnsupportedTagException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

@Service
public class AudiobookService {
    private final AuthorRepository authorRepository;
    private final BookRepository bookRepository;
    private final TagRepository tagRepository;
    private final UserRepository userRepository;
    private Logger log = LoggerFactory.getLogger(getClass());

    public AudiobookService(AuthorRepository authorRepository, BookRepository bookRepository, TagRepository tagRepository, UserRepository userRepository) {
        this.authorRepository = authorRepository;
        this.bookRepository = bookRepository;
        this.tagRepository = tagRepository;
        this.userRepository = userRepository;
    }

    @Transactional
    public void addAudiobook(String originalFilename, Path file) throws InvalidDataException, IOException, UnsupportedTagException {
        Book book = new Book();
        Mp3File mp3file = new Mp3File(file);
        String authorName = "";

        book.setLength(mp3file.getLengthInMilliseconds());
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
        tagRepository.removeUnusedTags();
    }

    @Transactional
    public void deleteAudiobook(Long id) {
        Book book = bookRepository.findBookById(id);
        Path data = Paths.get("data", book.getFilename());

        try {
            Files.deleteIfExists(data);
        } catch (IOException e) {
            log.error("The file:{} corresponding to the Audiobook: {} couldn't be deleted.", book.getFilename(), book.getTitle());
        }

        bookRepository.deleteBookById(id);
    }

    @Transactional
    public List<Book> getAllowedBooksByUsername(String username) {
        Set<Book> bookSet = new HashSet<>();
        User user = userRepository.findUserByUsername(username);
        for (Tag tag : user.getTags()) {
            tag.getBooks().forEach(book -> bookSet.add(book));
        }
        return new ArrayList<>(bookSet);
    }

}
