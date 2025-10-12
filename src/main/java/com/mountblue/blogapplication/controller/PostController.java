package com.mountblue.blogapplication.controller;

import com.mountblue.blogapplication.dto.PostRequest;
import com.mountblue.blogapplication.model.Comment;
import com.mountblue.blogapplication.model.Post;
import com.mountblue.blogapplication.service.PostService;
import lombok.extern.slf4j.Slf4j;
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

        return "view_all_post";
    }

    @GetMapping("/newpost")
    public String showPostForm(Model model) {
        model.addAttribute("postRequest", new PostRequest());
        return "create_post";
    }

    @PostMapping("/newpost")
    public String savePost(@ModelAttribute PostRequest postRequest) {
        List<String> tagList = postRequest.getTagList();
        Post post = postRequest.getPost();

        log.debug("{}", postRequest);
        log.debug("{}", post);
        log.debug("{}", tagList);

        Post savedPost = postService.savePost(post, tagList);
        return "redirect:/post/" + savedPost.getId();
    }

    @GetMapping("/post/{id}")
    public String getPostById(@PathVariable("id") Long id, Model model) {
        Post post = postService.getPostById(id);
        model.addAttribute("post", post);
        return "view_post";
    }

    @GetMapping("/post/{id}/edit")
    public String showEditForm(@PathVariable Long id, Model model) {
        Post post = postService.getPostById(id);
        PostRequest postRequest = new PostRequest(post);
        model.addAttribute("postRequest", postRequest);
        return "update_post";
    }

    @PutMapping("/post/{id}")
    public String updatePost(@ModelAttribute PostRequest postRequest, @PathVariable("id") Long id) {
        Post post = postRequest.getPost();
//        log.debug("{}", post);
        Post updatedPost = postService.updatePostById(id, post);
//        log.debug("{}", updatedPost);
        return "redirect:/post/" + updatedPost.getId();
    }

    @DeleteMapping("/post/{id}")
    public String deletePost(@PathVariable Long id) {
        postService.deletePost(id);
        return "redirect:/";
    }
}

