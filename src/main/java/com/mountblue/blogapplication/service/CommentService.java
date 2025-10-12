package com.mountblue.blogapplication.service;

import com.mountblue.blogapplication.model.Comment;
import com.mountblue.blogapplication.model.Post;
import com.mountblue.blogapplication.repository.CommentRepository;
import com.mountblue.blogapplication.repository.PostRepository;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PostMapping;

import static org.springframework.data.jpa.domain.AbstractPersistable_.id;

@Slf4j
@Service
public class CommentService {
    private final CommentRepository commentRepository;
    private final PostRepository postRepository;

    public CommentService(CommentRepository commentRepository, PostRepository postRepository) {
        this.commentRepository = commentRepository;
        this.postRepository = postRepository;
    }

    public void addComment(Long postId, Comment comment) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new RuntimeException("Post not found"));
        comment.setPost(post);
        post.getComments().add(comment);
        log.debug("{}", post);
        postRepository.save(post);
    }

    public Comment getCommentById(Long commentId) {
        return commentRepository.findById(commentId)
                .orElseThrow(() -> new RuntimeException("Comment not found with id: " + id));
    }

    public Comment updateComment(Comment comment) {
        Comment existingComment = commentRepository.findById(comment.getId())
                .orElseThrow(() -> new RuntimeException(
                        "Comment not found with id: " + comment.getId()
                ));

        existingComment.setName(comment.getName());
        existingComment.setEmail(comment.getEmail());
        existingComment.setComment(comment.getComment());

        if (comment.getPost() != null) {
            existingComment.setPost(comment.getPost());
        }

        return commentRepository.save(existingComment);
    }

    public Comment deleteComment(Long commentId) {
        return commentRepository.findById(commentId)
                .orElseThrow(() -> new RuntimeException(
                        "Comment not found with id: " + commentId
                ));
    }
}
