package ch.fhnw.webec;

import ch.fhnw.webec.entity.Author;
import ch.fhnw.webec.entity.Book;
import ch.fhnw.webec.repository.AuthorRepository;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.ArrayList;
import java.util.List;

@SpringBootApplication
public class GradedExerciseApplication {

    public static void main(String[] args) {
        SpringApplication.run(GradedExerciseApplication.class, args);
    }

}
