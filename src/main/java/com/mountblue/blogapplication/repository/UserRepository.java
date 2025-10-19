package com.mountblue.blogapplication.repository;

import com.mountblue.blogapplication.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);
    Optional<Object> findByName(String username);
    List<User> findByRole(String role);

}


