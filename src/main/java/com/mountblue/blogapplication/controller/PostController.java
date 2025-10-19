package com.mountblue.blogapplication.controller;

import com.mountblue.blogapplication.dto.PostFilterDTO;
import com.mountblue.blogapplication.dto.PostRequestDTO;
import com.mountblue.blogapplication.model.Post;
import com.mountblue.blogapplication.model.Tag;
import com.mountblue.blogapplication.model.User;
import com.mountblue.blogapplication.service.PostService;
import com.mountblue.blogapplication.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;

@Slf4j
@Controller
public class PostController {
    private final PostService postService;
    private final UserService userService;
    public PostController(PostService postService, UserService userService) {
        this.postService = postService;
        this.userService = userService;
    }

    @GetMapping("/")
    public java.lang.String showAllPost(@ModelAttribute PostFilterDTO filterDTO, Model model) {
        User currentUser = userService.getCurrentUser();
        Page<Post> filteredPost = postService.getFilteredPosts(filterDTO);
        List<User> authors = userService.findAuthors();
        Set<Tag> tags = postService.getAllTags();

        log.debug("filteredPost {}", filteredPost);
        model.addAttribute("currentUser", currentUser);
        model.addAttribute("filterDTO", filterDTO);
        model.addAttribute("filteredPost", filteredPost);
        model.addAttribute("authors", authors);
        model.addAttribute("tags", tags);

        return "view_all_post";
    }

    @GetMapping("/post/create")
    public String createPostForm(Model model) {
        User currentUser = userService.getCurrentUser();
        if (currentUser != null && currentUser.getRole().equals("ADMIN")) {
            List<User> authors = userService.findAuthors();
            model.addAttribute("authors", authors);
        }

        log.debug("Current User {}", currentUser);
        model.addAttribute("currentUser", currentUser);
        model.addAttribute("postRequestDTO", new PostRequestDTO());

        return "create_post";
    }

    @PostMapping("/post")
    public String savePost(@ModelAttribute PostRequestDTO postRequestDTO) {
        List<java.lang.String> tagList = postRequestDTO.getTagList();
        Post post = postRequestDTO.getPost();

        log.debug("{}", postRequestDTO);
        log.debug("{}", post);
        log.debug("{}", tagList);

        Post savedPost = postService.savePost(post, tagList);
        return "redirect:/post/" + savedPost.getId();
    }

    @GetMapping("/post/{id}")
    public String getPostById(@PathVariable("id") Long id, Model model) {
        Post post = postService.getPostById(id);
        log.debug("post Data{}", post);
        model.addAttribute("post", post);
        log.debug("{}", post);
        return "view_post";
    }

    // We can write this inside the controller
    // we are just calling this function that is present in the userService
    // Based on the Comment ID (Parameters present in the url)
    @PreAuthorize("@userService.isValidUserForPost(#id)")
    @GetMapping("/post/{id}/edit")
    public String showEditForm(@PathVariable Long id, Model model) {
        Post post = postService.getPostById(id);
        PostRequestDTO postRequestDTO = new PostRequestDTO(post);
        model.addAttribute("postRequestDTO", postRequestDTO);
        log.debug("{}", postRequestDTO);
        return "update_post";
    }

    @PreAuthorize("@userService.isValidUserForPost(#id)")
    @PutMapping("/post/{id}")
    public String updatePost(@ModelAttribute PostRequestDTO postRequestDTO, @PathVariable("id") Long id) {
        log.debug("{} {}", postRequestDTO, id);

        Post post = postRequestDTO.getPost();
        List<java.lang.String> tagList = postRequestDTO.getTagList();

        log.debug("{} {}", post, tagList);

        Post updatedPost = postService.updatePostById(id, post, tagList);

        return "redirect:/post/" + updatedPost.getId();
    }

    @PreAuthorize("@userService.isValidUserForPost(#id)")
    @DeleteMapping("/post/{id}")
    public String deletePost(@PathVariable Long id) {
        postService.deletePost(id);
        return "redirect:/";
    }

}

