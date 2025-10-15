package com.mountblue.blogapplication.service;

import com.mountblue.blogapplication.model.Comment;
import com.mountblue.blogapplication.model.Role;
import com.mountblue.blogapplication.model.User;
import com.mountblue.blogapplication.repository.CommentRepository;
import com.mountblue.blogapplication.repository.PostRepository;
import com.mountblue.blogapplication.repository.UserRepository;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {
    private final UserRepository userRepository;
    private final PostRepository postRepository;
    private final CommentRepository commentRepository;

    public UserService(UserRepository userRepository, PostRepository postRepository, CommentRepository commentRepository) {
        this.userRepository = userRepository;
        this.postRepository = postRepository;
        this.commentRepository = commentRepository;
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

    public boolean isValidUserForPost(Long postId) {
        User currentUser = getCurrentUser();
        if (currentUser == null) {
            return false;
        }
        return postRepository.findById(postId)
                .map(post -> post.getAuthor().getName().equals(currentUser.getName()) ||
                        userIsAdmin())
                .orElse(false);
    }

    private boolean userIsAdmin() {
        User currentUser = getCurrentUser();
        if (currentUser == null) {
            return false;
        }
        return currentUser.getRole() == Role.ADMIN;
    }

    public boolean isValidUserForComment(Long commentId) {
        User currentUser = getCurrentUser(); // your existing method
        if (currentUser == null) return false;

        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new RuntimeException("Comment not found"));

        return currentUser.getId().equals(comment.getPost().getAuthor().getId()) ||
                userIsAdmin();
    }

}
