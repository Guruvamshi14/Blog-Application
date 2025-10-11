package com.mountblue.blogapplication.controller;

import com.mountblue.blogapplication.model.Comment;
import com.mountblue.blogapplication.service.CommentService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@Slf4j
@Controller
public class CommentController {
    private CommentService commentService;
    public CommentController(CommentService commentService) {
        this.commentService = commentService;
    }

    @PostMapping("/post{postId}/comments")
    public String addComment(@PathVariable Long postId, @RequestBody Comment comment) {
        log.debug("{}", postId);
        log.debug("{}", comment);
        commentService.addComment(postId, comment);
        return "redirect:/post" + postId;
    }
}
