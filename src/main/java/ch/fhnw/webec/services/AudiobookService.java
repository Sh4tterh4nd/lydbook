package ch.fhnw.webec.services;

import ch.fhnw.webec.entity.Author;
import ch.fhnw.webec.entity.Book;
import ch.fhnw.webec.entity.Tag;
import ch.fhnw.webec.entity.User;
import ch.fhnw.webec.repository.AuthorRepository;
import ch.fhnw.webec.repository.BookRepository;
import ch.fhnw.webec.repository.TagRepository;
import ch.fhnw.webec.repository.UserRepository;
import com.mpatric.mp3agic.*;
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
    public Book addAudiobook(String uuidName, Path file) throws InvalidDataException, UnsupportedTagException, IOException {
        Book book = new Book();
        Mp3File mp3file = new Mp3File(file);
        String authorName = "";

        book.setDataName(uuidName);
        if (mp3file.hasId3v2Tag()) {
            ID3v2 id3v2Tag = mp3file.getId3v2Tag();
            book.setTitle(id3v2Tag.getAlbum());
            authorName = id3v2Tag.getArtist();
            byte[] cover = id3v2Tag.getAlbumImage();
            if (cover != null) {
                try {
                    Files.write(Paths.get("data", uuidName.concat(".jpeg")), cover);
                } catch (Exception e) {
                    log.error("Couldn't write cover image for book {}.", book.getTitle());
                }
            }

        } else if (mp3file.hasId3v1Tag()) {
            ID3v1 id3v1Tag = mp3file.getId3v1Tag();
            book.setTitle(id3v1Tag.getAlbum());
            authorName = id3v1Tag.getArtist();
        }
//        System.out.println(mp3file.isVbr());


        Author a = authorRepository.findOrCreateAuthorByName(authorName);
        Tag authorTag = tagRepository.findOrCreateFirstByName(a.getName(), false);
        Tag audiobookTag = tagRepository.findOrCreateFirstByName("Audiobook", false);

        book.addTag(authorTag);
        book.addTag(audiobookTag);
        book.setAuthor(a);
        bookRepository.save(book);


        log.info("Audiobook: {} by {} has been added.", book.getTitle(), book.getAuthor().getName());
        return book;
    }

    public void updateAudiobookAndTags(Book updatedBook){
        updateAudiobook(updatedBook);
        removeAllUnusedTags();
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
        log.info("Audiobook: {} has been updated.", oldBook.getTitle());
    }

    public void removeAllUnusedTags(){
        List<Tag> tagsWithNoBooks = tagRepository.findTagsWithNoBooks();
        List<User> allUsers = userRepository.findAllByOrderByUsernameAsc();
        for (User user : allUsers) {
            for (Tag tagWithNoBook : tagsWithNoBooks) {
                user.getTags().remove(tagWithNoBook);
            }
        }

        allUsers.forEach(user -> userRepository.save(user));


        for (User user : userRepository.findAllByOrderByUsernameAsc()) {
            System.out.println(user);
        }
        tagRepository.removeTagsWithNoBooks();
    }

    @Transactional
    public void deleteAudiobook(Long id) {
        Book book = bookRepository.findBookById(id);
        Path data = Paths.get("data", book.getDataName().concat(".mp3"));

        try {
            Files.deleteIfExists(data);
            Files.deleteIfExists(Paths.get("data", book.getDataName().concat(".jpeg")));
        } catch (IOException e) {
            log.error("The file:{} corresponding to the Audiobook: {} couldn't be deleted.", book.getDataName(), book.getTitle());
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
        List<Book> bookList = new ArrayList<>(bookSet);
        bookList.sort(Comparator.comparing(Book::getTitle));
        return bookList;
    }

    public Book findAllowedBookByIdAndUsername(Long id,String username){
        User user = userRepository.findUserByUsername(username);

        for (Tag tag : user.getTags()) {
            for (Book book : tag.getBooks()) {
                if (book.getId().equals(id)){
                    return book;
                }
            }
        }
        return null;
    }

}
