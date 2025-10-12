package com.mountblue.blogapplication.controller;

import com.mountblue.blogapplication.model.Comment;
import com.mountblue.blogapplication.service.CommentService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Slf4j
@Controller
public class CommentController {
    private CommentService commentService;
    public CommentController(CommentService commentService) {
        this.commentService = commentService;
    }

    @PostMapping("/post{postId}/comments")
    public String addComment(@PathVariable Long postId, @RequestBody Comment comment) {
//        log.debug("{}", postId);
//        log.debug("{}", comment);
        commentService.addComment(postId, comment);
        return "dumb";
    }

    @GetMapping("/update/comment{commentId}")
    public String showEditForm(@PathVariable Long commentId, Model model) {
        Comment comment = commentService.getCommentById(commentId);
        model.addAttribute("comment", comment);
        return "editComment";
    }

    @PutMapping("/post/comment{commentId}")
    public String updateComment(@PathVariable Long commentId,
                                @RequestBody Comment comment) {
        comment.setId(commentId);

        Comment updated = commentService.updateComment(comment);
        return "redirect:/post" + updated.getPost().getId();
    }

    @DeleteMapping("/post/comment{commentId}")
    public String deleteComment(@PathVariable Long commentId) {
        commentService.getCommentById(commentId);
        return "redirect:/";
    }

}
