package com.mountblue.blogapplication.dto;

import com.mountblue.blogapplication.model.Post;
import com.mountblue.blogapplication.model.Tag;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.lang.reflect.Array;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PostRequest {
    private String title;
    private String content;
    private String author;
    private String excerpt;
    private String tags;
    private LocalDateTime publishedAt;


    // Try to Add the Manual Bean
    public Post getPost() {
        Post post = new Post();
        post.setTitle(title);
        post.setContent(content);
        post.setAuthor(author);
        post.setExcerpt(excerpt);
        post.setPublishedAt(publishedAt);
        return post;
    }

    public List<String> getTagList() {
        return Arrays.stream(tags.split(","))
                                    .map(String::trim)
                                    .filter(tag -> !tag.isEmpty())
                                    .distinct()
                                    .toList();
    }

    public static String convertTagsToString(Set<Tag> tagsSet) {
        if (tagsSet == null || tagsSet.isEmpty()) {
            return "";
        }
        return tagsSet.stream()
                .map(Tag::getName)
                .filter(Objects::nonNull)
                .collect(Collectors.joining(", "));
    }
}
