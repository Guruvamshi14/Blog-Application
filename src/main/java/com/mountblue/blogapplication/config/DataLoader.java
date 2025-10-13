package com.mountblue.blogapplication.config;

import com.mountblue.blogapplication.model.Post;
import com.mountblue.blogapplication.service.PostService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

@Slf4j
@Component
public class DataLoader implements CommandLineRunner {

    private final PostService postService;

    public DataLoader(PostService postService) {
        this.postService = postService;
    }

    @Override
    public void run(String... args) throws Exception {

        List<String> authors = Arrays.asList("AB Devillers", "Kohli", "RCB Fan", "Java Guru", "Spring Master");
        List<String> tagsPool = Arrays.asList("RCB", "IPL", "Spring Boot", "JAVA", "Cricket");

        for (int i = 1; i <= 30; i++) {
            Post post = new Post();
            post.setTitle("Sample Post " + i);
            post.setExcerpt("This is excerpt for post " + i);
            post.setContent("This is the content for post " + i + ". It covers topics like " + tagsPool.get(i % tagsPool.size()));
            post.setAuthor(authors.get(i % authors.size()));
            post.setPublishedAt(LocalDateTime.now().minusDays(30 - i));

            // Pick two tags
            List<String> postTags = Arrays.asList(tagsPool.get(i % tagsPool.size()), tagsPool.get((i + 1) % tagsPool.size()));

            postService.savePost(post, postTags);
        }

        log.debug("{}", "30 sample posts inserted successfully!");
    }
}

