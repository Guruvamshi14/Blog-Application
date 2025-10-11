package com.mountblue.blogapplication.repository;

import com.mountblue.blogapplication.model.Comment;
import com.mountblue.blogapplication.model.Post;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Long> {
    List<Comment> findByPost(Post post);
}
