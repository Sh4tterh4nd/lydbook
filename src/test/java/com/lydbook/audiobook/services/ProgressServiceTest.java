package com.lydbook.audiobook.services;

import com.lydbook.audiobook.entity.Book;
import com.lydbook.audiobook.entity.Progress;
import com.lydbook.audiobook.entity.User;
import com.lydbook.audiobook.repository.book.BookRepository;
import com.lydbook.audiobook.repository.ProgressRepository;
import com.lydbook.audiobook.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import static org.junit.jupiter.api.Assertions.*;
@EnableAutoConfiguration
@SpringBootTest()
@ActiveProfiles("test")
class ProgressServiceTest {

    @Autowired
    private ProgressService progressService;

    @Autowired
    private ProgressRepository progressRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private BookRepository bookRepository;

    @Test
    void updateProgress() {
        progressService.updateProgress("test2",1001L, 500);
        User test2 = userRepository.findUserByUsername("test2");
        Book book = bookRepository.findBookById(1001L);
        Progress firstByBookAndUser = progressRepository.findFirstByBookAndUser(book, test2);

        assertEquals(firstByBookAndUser.getProgress(),500);
    }

    @Test
    void getMostRecentProgressByUser() {
        progressService.updateProgress("test2",1001L, 500);
        progressService.updateProgress("test2",1000L, 222);

  assertEquals( progressService.getMostRecentProgressByUser("test2").get(0).getProgress(),222);

    }
}