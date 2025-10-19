package com.mountblue.blogapplication.restcontroller;

import com.mountblue.blogapplication.dto.CommentDTO;
import com.mountblue.blogapplication.model.Comment;
import com.mountblue.blogapplication.service.CommentService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/api")
public class CommentRestController {
    private final CommentService commentService;
    public CommentRestController(CommentService commentService) {
        this.commentService = commentService;
    }

    @PostMapping("/post/{postId}/comments")
    public CommentDTO addComment(@PathVariable Long postId, @RequestParam String name,
                             @RequestParam String email, @RequestParam String commentText) {
        log.debug("{} {}", name, postId);
        Comment savedComment = commentService.saveComment(postId, name, email, commentText);
        return CommentDTO.convertCommentDTO(savedComment);
    }

    @PreAuthorize("@userService.isValidUserForComment(#commentId)")
    @PutMapping("/comment/{commentId}")
    public CommentDTO updateComment(@PathVariable Long commentId, @RequestParam String name,
                                @RequestParam String email, @RequestParam String commentText) {
        Comment updatedComment = commentService.updateComment(commentId, name, email, commentText);
        return CommentDTO.convertCommentDTO(updatedComment);
    }

    @PreAuthorize("@userService.isValidUserForComment(#commentId)")
    @DeleteMapping("/comment/{commentId}")
    public void deleteComment(@PathVariable Long commentId) {
        commentService.deleteComment(commentId);
    }
}


