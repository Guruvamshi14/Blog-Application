package com.mountblue.blogapplication.service;

import com.mountblue.blogapplication.model.Role;
import com.mountblue.blogapplication.model.User;
import com.mountblue.blogapplication.repository.UserRepository;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User getCurrentUser() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        if (auth != null && auth.isAuthenticated() && !"anonymousUser".equals(auth.getPrincipal())) {
            String username = auth.getName(); // username used to login
            return (User) userRepository.findByName(username).orElse(null);
        }
        return null;
    }

    public List<User> findAll() {
        return userRepository.findAll();
    }

    public List<User> findAuthors() {
        return userRepository.findByRole(Role.AUTHOR);
    }

}
