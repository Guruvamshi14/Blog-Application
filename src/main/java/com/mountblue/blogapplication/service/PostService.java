package com.mountblue.blogapplication.service;


import com.mountblue.blogapplication.model.Post;
import com.mountblue.blogapplication.model.Tag;
import com.mountblue.blogapplication.repository.PostRepository;
import com.mountblue.blogapplication.repository.TagRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Slf4j
@Service
@RequiredArgsConstructor
public class PostService {
    private final PostRepository postRepository;
    private final TagRepository tagRepository;

    public void savePost(Post post, List<String> tagList) {
        Set<Tag> tags = new HashSet<>();
        for (String tagName : tagList) {
            Tag tag = tagRepository.findByName(tagName)
                    .orElseGet(() -> new Tag(tagName));
            tags.add(tag);
        }
        post.setTags(tags);
        postRepository.save(post);
    }

    public List<Post> getAllPosts() {
        return postRepository.findAll();
    }

    public Page<Post> getPaginatedPosts(int pageNo, int pageSize) {
        PageRequest pageable = PageRequest.of(pageNo, pageSize);
        return postRepository.findAll(pageable);
    }

    public Post getPostById(Long id) {
        return postRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Post not found with id: " + id));
    }

    public void deletePost(Long id) {
        postRepository.deleteById(id);
    }

    // We can Merge this into the Save Function for ReUsable of code
    public Post updatePostById(Long id, Post updatedPost) {
        Post existingPost = postRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Post not found with id: " + id));

        existingPost.setTitle(updatedPost.getTitle());
        existingPost.setAuthor(updatedPost.getAuthor());
        existingPost.setContent(updatedPost.getContent());
        existingPost.setExcerpt(updatedPost.getExcerpt());
        existingPost.setPublishedAt(updatedPost.getPublishedAt());

        return postRepository.save(existingPost);
    }

}
