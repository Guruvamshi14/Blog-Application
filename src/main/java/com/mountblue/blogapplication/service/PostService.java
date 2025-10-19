package com.mountblue.blogapplication.service;


import com.mountblue.blogapplication.dto.PostFilterDTO;
import com.mountblue.blogapplication.model.Post;
import com.mountblue.blogapplication.model.Tag;
import com.mountblue.blogapplication.model.User;
import com.mountblue.blogapplication.repository.PostRepository;
import com.mountblue.blogapplication.repository.TagRepository;
import com.mountblue.blogapplication.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Slf4j
@Service
@RequiredArgsConstructor
public class PostService {
    private final PostRepository postRepository;
    private final TagRepository tagRepository;
    private final UserRepository userRepository;

    @Transactional
    public Post savePost(Post post, List<String> tagList) {
        Set<Tag> tags = new HashSet<>();
        for (String tagName : tagList) {
            Tag tag = tagRepository.findByName(tagName)
                    .orElseGet(() -> new Tag(tagName));
            tags.add(tag);
        }

        if (post.getAuthor() != null && post.getAuthor().getId() != null) {
            User fullAuthor = userRepository.findById(post.getAuthor().getId())
                    .orElseThrow(() -> new RuntimeException("Author not found"));
            post.setAuthor(fullAuthor);
        }

        post.setTags(tags);
        return postRepository.save(post);
    }

    public List<Post> getAllPosts() {
        return postRepository.findAll();
    }

    public Post getPostById(Long id) {
        return postRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Post not found with id: " + id));
    }

    public void deletePost(Long id) {
        postRepository.deleteById(id);
    }

    public Post updatePostById(Long id, Post updatedPost, List<String> tagList) {
        Post existingPost = postRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Post not found with id: " + id));

        Set<Tag> tags = new HashSet<>();
        for (String tagName : tagList) {
            Tag tag = tagRepository.findByName(tagName)
                    .orElseGet(() -> new Tag(tagName));
            tags.add(tag);
        }

        if (existingPost.getAuthor() != null && existingPost.getAuthor().getId() != null) {
            User fullAuthor = userRepository.findById(existingPost.getAuthor().getId())
                    .orElseThrow(() -> new RuntimeException("Author not found"));
            existingPost.setAuthor(fullAuthor);
        }

        existingPost.setTags(tags);
        existingPost.setTitle(updatedPost.getTitle());
        existingPost.setAuthor(updatedPost.getAuthor());
        existingPost.setContent(updatedPost.getContent());
        existingPost.setExcerpt(updatedPost.getExcerpt());

        return postRepository.save(existingPost);
    }

    public Page<Post> getFilteredPosts(PostFilterDTO filterDTO) {
        Sort sort = filterDTO.getOrder().equalsIgnoreCase("asc") ?
                Sort.by(filterDTO.getSortField()).ascending() :
                Sort.by(filterDTO.getSortField()).descending();

        Pageable pageable = PageRequest.of(filterDTO.getPage(), filterDTO.getSize(), sort);

        List<String> authors = (filterDTO.getAuthors() != null && !filterDTO.getAuthors().isEmpty())
                ? filterDTO.getAuthors()
                : null;

        List<String> tags = (filterDTO.getTags() != null && !filterDTO.getTags().isEmpty())
                ? filterDTO.getTags()
                : null;

        long tagCount = (tags != null) ? tags.size() : 0;

        String search = (filterDTO.getSearch() != null && !filterDTO.getSearch().isEmpty())
                ? filterDTO.getSearch()
                : null;

        LocalDateTime start = filterDTO.getStartPublishDate() != null
                ? filterDTO.getStartPublishDate().atStartOfDay()
                : null;

        LocalDateTime end = filterDTO.getEndPublishDate() != null
                ? filterDTO.getEndPublishDate().atTime(23, 59, 59)
                : null;

        return postRepository.findFilteredPosts( authors, tags, tagCount, search,
                start, end, pageable);
    }

    public Set<Tag> getAllTags() {
        return new HashSet<>(postRepository.findAllTags());
    }

}
