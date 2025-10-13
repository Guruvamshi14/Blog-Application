package com.mountblue.blogapplication.service;

import com.mountblue.blogapplication.model.Comment;
import com.mountblue.blogapplication.model.Post;
import com.mountblue.blogapplication.repository.CommentRepository;
import com.mountblue.blogapplication.repository.PostRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import static org.springframework.data.jpa.domain.AbstractPersistable_.id;

@Slf4j
@Service
public class CommentService {
    private final CommentRepository commentRepository;
    private final PostRepository postRepository;

    private static final String ACTION_1 = "Comment not found with id: ";
    public CommentService(CommentRepository commentRepository, PostRepository postRepository) {
        this.commentRepository = commentRepository;
        this.postRepository = postRepository;
    }

    public void addComment(Long postId, String name, String email, String commentText) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new RuntimeException("Post not found"));

        Comment newComment = new Comment();
        newComment.setPost(post);
        newComment.setComment(commentText);
        newComment.setName(name);
        newComment.setEmail(email);
        post.getComments().add(newComment);

        postRepository.save(post);
    }

    public Comment getCommentById(Long commentId) {
        return commentRepository.findById(commentId)
                .orElseThrow(() -> new RuntimeException(ACTION_1 + id));
    }

    public Comment updateComment(Long commentId, String name, String email, String commentText)  {
        Comment existingComment = commentRepository.findById(commentId)
                .orElseThrow(() -> new RuntimeException(ACTION_1 + commentId));

        existingComment.setName(name);
        existingComment.setEmail(email);
        existingComment.setComment(commentText);

        return commentRepository.save(existingComment);
    }

    public String deleteComment(Long commentId) {
        Comment existingComment = commentRepository.findById(commentId)
                .orElseThrow(() -> new RuntimeException(
                        ACTION_1 + commentId
                ));

        Long postId = existingComment.getPost().getId();
        commentRepository.deleteById(commentId);

        return "redirect:/post/" + postId;
    }

}
