package com.mountblue.blogapplication.controller;

import com.mountblue.blogapplication.model.Comment;
import com.mountblue.blogapplication.service.CommentService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Slf4j
@Controller
public class CommentController {
    private final CommentService commentService;
    public CommentController(CommentService commentService) {
        this.commentService = commentService;
    }

    @PostMapping("/post/{postId}/comments")
    public String addComment(@PathVariable Long postId, @RequestParam String name,
                             @RequestParam String email, @RequestParam String commentText) {

        log.debug("{} {}", name, postId);
        commentService.addComment(postId, name, email, commentText);
        return "redirect:/post/" + postId;
    }

    @PreAuthorize("@userService.isValidUserForComment(#commentId)")
    @GetMapping("/comment/{commentId}/edit")
    public String showEditForm(@PathVariable Long commentId, Model model) {
        Comment comment = commentService.getCommentById(commentId);
        model.addAttribute("comment", comment);
        return "edit_comment";
    }

    @PreAuthorize("@userService.isValidUserForComment(#commentId)")
    @PutMapping("/comment/{commentId}")
    public String updateComment(@PathVariable Long commentId, @RequestParam String name,
                                @RequestParam String email, @RequestParam String commentText) {

        Comment updatedComment = commentService.updateComment(commentId, name, email, commentText);
        return "redirect:/post/" + updatedComment.getPost().getId();
    }

    @PreAuthorize("@userService.isValidUserForComment(#commentId)")
    @DeleteMapping("/comment/{commentId}")
    public String deleteComment(@PathVariable Long commentId) {
        return commentService.deleteComment(commentId);
    }
}
