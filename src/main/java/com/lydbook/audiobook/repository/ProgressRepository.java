package com.lydbook.audiobook.repository;

import com.lydbook.audiobook.entity.Book;
import com.lydbook.audiobook.entity.Progress;
import com.lydbook.audiobook.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProgressRepository extends JpaRepository<Progress,Long> {

    List<Progress> findProgressesByUser(User user);
    Progress findFirstByBookAndUser(Book book, User user);
}
