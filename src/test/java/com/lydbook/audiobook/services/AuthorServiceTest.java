package com.lydbook.audiobook.services;

import com.lydbook.audiobook.entity.Author;
import com.lydbook.audiobook.repository.BookRepository;
import com.lydbook.audiobook.repository.ProgressRepository;
import com.lydbook.audiobook.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@EnableAutoConfiguration
@SpringBootTest()
@ActiveProfiles("test")
class AuthorServiceTest {
    @Autowired
    private AuthorService authorService;

    @Autowired
    private ProgressRepository progressRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private BookRepository bookRepository;


    @Test
    void getAllowedAuthorsByUsername() {
        List<Author> test3 = authorService.getAllowedAuthorsByUsername("test3");

        assertTrue(test3.size()==1);
        assertEquals(test3.get(0).getName(),"Brandon Sanderson");
    }

    @Test
    void getAllowedAuthorByIdAndUsername_actuallyOwned() {
        Author test3 = authorService.getAllowedAuthorByIdAndUsername(1000L, "test3");
        assertEquals(test3.getName(), "Brandon Sanderson");
    }
    @Test
    void getAllowedAuthorByIdAndUsername_noPermission() {
        Author test3 = authorService.getAllowedAuthorByIdAndUsername(1001L, "test3");
        assertNull(test3);
    }
}