package com.mountblue.blogapplication.restcontroller;


import com.mountblue.blogapplication.dto.PostDTO;
import com.mountblue.blogapplication.dto.PostFilterDTO;
import com.mountblue.blogapplication.dto.PostRequestDTO;
import com.mountblue.blogapplication.model.Post;
import com.mountblue.blogapplication.service.PostService;
import com.mountblue.blogapplication.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/api")
public class PostRestController {
    private final PostService postService;
    private final UserService userService;
    public PostRestController(PostService postService, UserService userService) {
        this.postService = postService;
        this.userService = userService;
    }

    @GetMapping("/")
    public Map<String, Object> getFilteredPosts(@RequestBody PostFilterDTO filterDTO) {
        Page<Post> filteredPosts = postService.getFilteredPosts(filterDTO);
        List<PostDTO> postDTOs = filteredPosts.stream()
                .map(PostDTO::convertPostDTO)
                .toList();

        Map<String, Object> response = new HashMap<>();
        response.put("posts", postDTOs);
        response.put("currentPage", filteredPosts.getNumber());
        response.put("totalItems", filteredPosts.getTotalElements());
        response.put("totalPages", filteredPosts.getTotalPages());

        return response;
    }

    @PostMapping("/post")
    public PostDTO savePost(@RequestBody PostRequestDTO postRequestDTO) {
        List<String> tagList = postRequestDTO.getTagList();
        Post post = postRequestDTO.getPost();
        Post savedPost = postService.savePost(post, tagList);
        return PostDTO.convertPostDTO(savedPost);
    }

    @GetMapping("/post/{id}")
    public PostDTO getPostById(@PathVariable("id") Long id) {
        Post post = postService.getPostById(id);
        return PostDTO.convertPostDTO(post);
    }

    @PreAuthorize("@userService.isValidUserForPost(#id)")
    @PutMapping("/post/{id}")
    public PostDTO updatePost(@RequestBody PostRequestDTO postRequestDTO, @PathVariable("id") Long id) {
        Post post = postRequestDTO.getPost();
        List<String> tagList = postRequestDTO.getTagList();
        Post updatedPost = postService.updatePostById(id, post, tagList);
        return PostDTO.convertPostDTO(updatedPost);
    }

    @PreAuthorize("@userService.isValidUserForPost(#id)")
    @DeleteMapping("/post/{id}")
    public void deletePost(@PathVariable Long id) {
        postService.deletePost(id);
    }
}

