package com.lydbook.audiobook.repository;

import com.lydbook.audiobook.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    User findUserById(Long id);
    User findUserByUsername(String username);
    List<User> findAllByOrderByUsernameAsc();
    void deleteUserById(Long id);
}
