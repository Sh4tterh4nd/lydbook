package ch.fhnw.webec.services;

import ch.fhnw.webec.entity.Book;
import ch.fhnw.webec.entity.Progress;
import ch.fhnw.webec.entity.User;
import ch.fhnw.webec.repository.BookRepository;
import ch.fhnw.webec.repository.ProgressRepository;
import ch.fhnw.webec.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;

/**
 * Progress service.
 */
@Service
public class ProgressService {
    private final ProgressRepository progressRepository;
    private final BookRepository bookRepository;
    private final UserRepository userRepository;
    private Logger log = LoggerFactory.getLogger(getClass());

    /**
     * Instantiates a new Progress service.
     *
     * @param progressService the progress service
     * @param bookRepository  the book repository
     * @param userRepository  the user repository
     */
    public ProgressService(ProgressRepository progressService, BookRepository bookRepository, UserRepository userRepository) {
        this.progressRepository = progressService;
        this.bookRepository = bookRepository;
        this.userRepository = userRepository;
    }


    /**
     * Update progress.
     *
     * @param username     the username
     * @param bookId       the book id
     * @param progressTime the progress time
     */
    public void updateProgress(String username, Long bookId, Integer progressTime) {
        Book book = bookRepository.findBookById(bookId);
        User user = userRepository.findUserByUsername(username);

        Progress progress = progressRepository.findFirstByBookAndUser(book, user);
        if (progress == null) {
            progress = new Progress();
            progress.setUser(user);
            progress.setBook(book);
        }
        progress.setProgress(progressTime);

        log.info("Progress updated for Book: {} from User {}", progress.getBook().getTitle(), progress.getUser().getUsername());
        progressRepository.save(progress);
    }

    /**
     * Get all progresses ordered by newest from a certain answer
     *
     * @param username the username
     * @return the list
     */
    public List<Progress> getMostRecentProgressByUser(String username){
        User userByUsername = userRepository.findUserByUsername(username);

        List<Progress> progressList = userByUsername.getProgressList();
        progressList.sort(Comparator.comparing(Progress::getUpdatedtime).reversed());

        return progressList;
    }
}
