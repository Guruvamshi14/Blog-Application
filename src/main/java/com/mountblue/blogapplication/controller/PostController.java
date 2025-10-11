package com.mountblue.blogapplication.controller;

import com.mountblue.blogapplication.dto.PostRequest;
import com.mountblue.blogapplication.model.Post;
import com.mountblue.blogapplication.service.PostService;
import com.mountblue.blogapplication.service.TagService;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.apache.bcel.generic.ClassGen;
import org.springframework.boot.Banner;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.ui.Model;

import java.util.List;

@Slf4j
@Controller
//@ResponseBody
public class PostController {
    private final PostService postService;
    private static final String VIEW_POST = "viewPost";
    public PostController(PostService postService) {
        this.postService = postService;
    }

    @GetMapping("/")
    public String showAllPost(@RequestParam(defaultValue = "0") int page, Model model) {
        Page<Post> posts =  postService.getPaginatedPosts(page, 4);
        log.debug("{}", posts);

        model.addAttribute("postPage", posts);
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", posts.getTotalPages());

        return "viewallpost";
    }

    @GetMapping("/newpost")
    public String showPostForm() {
        log.debug("I am in the post Form");
        return "new-post";
    }

    @PostMapping("/newpost")
    public String savePost(@RequestBody PostRequest postRequest, Model model) {
        log.debug("{}", postRequest);
        List<String> tagList = postRequest.getTagList();
        Post post = postRequest.getPost();
        postService.savePost(post, tagList);
        model.addAttribute("post", post);
        return VIEW_POST;
    }

    @GetMapping("/post{id}")
    public String getPostById(@PathVariable("id") Long id, Model model) {
        log.debug("{}", id);
        Post post = postService.getPostById(id);
        log.debug("{}", post);
        model.addAttribute("post", post);
        return VIEW_POST;
    }

    @GetMapping("/editpost{id}")
    public String showEditForm(@PathVariable Long id, Model model) {
        Post post = postService.getPostById(id);
        String tagNames = PostRequest.convertTagsToString(post.getTags());
        model.addAttribute("post", post);
        model.addAttribute("tagNames", tagNames);
        return "updatePost";
    }

    @PutMapping("/post{id}")
    public String updatePost(@RequestBody PostRequest postRequest, @PathVariable("id") Long id, Model model) {
        log.debug("In the Update Post");
        Post updatedPost = postRequest.getPost();
        Post post = postService.updatePostById(id, updatedPost);
        log.debug("{}", post);
        model.addAttribute("post", post);
        return VIEW_POST;
    }

    @DeleteMapping("/post{id}")
    public ResponseEntity<Void> deletePost(@PathVariable Long id) {
        log.debug("{}", id);
        postService.deletePost(id);
        return ResponseEntity.ok().build(); // send 200 OK
    }
}

