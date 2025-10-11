package com.mountblue.blogapplication.repository;

import com.mountblue.blogapplication.model.Post;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PostRepository extends JpaRepository<Post, Long> {
}