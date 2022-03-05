package com.lydbook.audiobook.services;

import com.lydbook.audiobook.config.AuthenticationFacade;
import com.lydbook.audiobook.config.IAuthenticationFacade;
import com.lydbook.audiobook.entity.Author;
import com.lydbook.audiobook.repository.book.BookRepository;
import com.lydbook.audiobook.repository.ProgressRepository;
import com.lydbook.audiobook.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
@EnableAutoConfiguration
@SpringBootTest()
@ActiveProfiles("test")
class AuthorServiceTest {
    @Autowired
    private AuthorService authorService;

    @Autowired
    private IAuthenticationFacade authenticationFacade;
    @Autowired
    private ProgressRepository progressRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private BookRepository bookRepository;


    @Test
    @WithMockUser("test3")
    void getAllowedAuthorsByUsername() {


        List<Author> test3 = authorService.getAllowedAuthorsByUsername();
        assertTrue(test3.size()==1);
        assertEquals(test3.get(0).getName(),"Brandon Sanderson");
    }

    @Test
    @WithMockUser("test3")
    void getAllowedAuthorByIdAndUsername_actuallyOwned() {
        Author test3 = authorService.getAllowedAuthorByIdAndUsername(1000L);
        assertEquals(test3.getName(), "Brandon Sanderson");
    }
    @Test
    @WithMockUser("test3")
    void getAllowedAuthorByIdAndUsername_noPermission() {
        Author test3 = authorService.getAllowedAuthorByIdAndUsername(1001L);
        assertNull(test3);
    }
}