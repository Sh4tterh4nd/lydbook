package com.lydbook.audiobook.services;

import com.lydbook.audiobook.entity.Author;
import com.lydbook.audiobook.entity.Book;
import com.lydbook.audiobook.entity.Tag;
import com.lydbook.audiobook.entity.User;
import com.lydbook.audiobook.repository.author.AuthorRepository;
import com.lydbook.audiobook.repository.book.BookRepository;
import com.lydbook.audiobook.repository.series.SeriesRepository;
import com.lydbook.audiobook.repository.tag.TagRepository;
import com.lydbook.audiobook.repository.UserRepository;
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

/**
 * Audiobook service
 */
@Service
public class BookService {
    private final AuthorRepository authorRepository;
    private final BookRepository bookRepository;
    private final TagRepository tagRepository;
    private final UserRepository userRepository;
    private final SeriesRepository seriesRepository;
    private Logger log = LoggerFactory.getLogger(getClass());

    /**
     * Instantiates a new Audiobook service.
     *
     * @param authorRepository the author repository
     * @param bookRepository   the book repository
     * @param tagRepository    the tag repository
     * @param userRepository   the user repository
     * @param seriesRepository
     */
    public BookService(AuthorRepository authorRepository, BookRepository bookRepository, TagRepository tagRepository, UserRepository userRepository, SeriesRepository seriesRepository) {
        this.authorRepository = authorRepository;
        this.bookRepository = bookRepository;
        this.tagRepository = tagRepository;
        this.userRepository = userRepository;
        this.seriesRepository = seriesRepository;
    }

    /**
     * Add new audiobook
     *
     * @param uuidName the uuid name
     * @param file     the file
     * @return the book
     * @throws InvalidDataException    the invalid data exception
     * @throws UnsupportedTagException the unsupported tag exception
     * @throws IOException             the io exception
     */
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

    /**
     * Update audiobook and remove all unused tags afet it.
     *
     * @param updatedBook the updated book
     */
    public void updateAudiobookAndTags(Book updatedBook) {
        updateAudiobook(updatedBook);
        removeAllUnusedTags();
        seriesRepository.removeAllUnusedSeries();
    }

    /**
     * Update audiobook.
     *
     * @param updatedBook the updated book
     */
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
        if (updatedBook.getSeries() != null && !updatedBook.getSeries().getName().isBlank()) {
            oldBook.setSeries(seriesRepository.findOrCreateSeries(updatedBook.getSeries().getName()));
            oldBook.setBookNumber(updatedBook.getBookNumber());
        }else {
            oldBook.setSeries(null);
        }

        bookRepository.save(oldBook);
        log.info("Audiobook: {} has been updated.", oldBook.getTitle());
    }

    /**
     * Remove all tags that are not owned by any audiobook anymore.
     */
    public void removeAllUnusedTags() {
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

    /**
     * Delete audiobook by id where
     *
     * @param id the id
     */
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
}
