package com.lydbook.audiobook.services;

import com.lydbook.audiobook.entity.Book;
import com.lydbook.audiobook.entity.Tag;
import com.lydbook.audiobook.repository.book.BookRepository;
import com.lydbook.audiobook.repository.tag.TagRepository;
import com.mpatric.mp3agic.InvalidDataException;
import com.mpatric.mp3agic.UnsupportedTagException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@EnableAutoConfiguration
@SpringBootTest()
@ActiveProfiles("test")
class BookServiceTest {
    private Path resources = Paths.get(getClass().getClassLoader().getResource("mp3").toURI());

    @Autowired
    private BookService bookService;

    @Autowired
    private BookRepository bookRepository;

    @Autowired
            private TagRepository tagRepository;

    BookServiceTest() throws URISyntaxException {
    }

    @Test
    void addAudiobook() throws InvalidDataException, IOException, UnsupportedTagException {
        Book book = bookService.addAudiobook("abc-abc", resources.resolve("FREE The Jester (A Riyria Chronicles Tale).mp3"));
        assertNotNull(book.getId());
    }

    @Test
    void updateAudiobook() {
        Book bookById = bookRepository.findBookById(1000L);
        bookById.addTag(new Tag("TestTag"));
        bookService.updateAudiobook(bookById);
        Book bookByIdFromDb = bookRepository.findBookById(1000L);
        assertTrue(bookByIdFromDb.getTags().contains(new Tag("TestTag")));
    }

    @Test
    void removeAllUnusedTags() {
        assertNotNull( tagRepository.findFirstByName("Test2"));
        bookService.removeAllUnusedTags();
        assertNull(tagRepository.findFirstByName("Test2"));
    }

    @Test
    void deleteAudiobook() {
        bookService.deleteAudiobook(1002L);
        assertNull(bookRepository.findBookById(1002L));
    }

    @Test
    void getAllowedBooksByUsername() {
        List<Book> test3Books = bookRepository.findAllowedBooksByUsername("test3");
        assertTrue(test3Books.size() == 1);
        assertEquals(test3Books.get(0).getTitle(), "Skyward");
    }

    @Test
    void getAllowedBookByIdAndUsername() {
        Book test3AllowedBook = bookRepository.findAllowedBookById(1000L, "test3");
        assertNotNull(test3AllowedBook);
        assertEquals(test3AllowedBook.getTitle(), "Skyward");
    }
}