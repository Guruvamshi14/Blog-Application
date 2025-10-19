package com.mountblue.blogapplication.dto;

import com.mountblue.blogapplication.model.Post;
import com.mountblue.blogapplication.model.Tag;
import com.mountblue.blogapplication.model.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.*;
import java.util.stream.Collectors;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PostRequest {
    private String title;
    private String content;
    private User author;
    private String excerpt;
    private String tags;
    private Long id;

    public PostRequest(Post post) {
        this.title = post.getTitle();
        this.content = post.getContent();
        this.author = post.getAuthor();
        this.excerpt = post.getExcerpt();
        this.tags = convertTagsToString(post.getTags());
        this.id = post.getId();
        this.author = post.getAuthor();
    }

    public Post getPost() {
        Post post = new Post();
        post.setTitle(title);
        post.setContent(content);
        post.setAuthor(author);
        post.setExcerpt(excerpt);
        post.setAuthor(author);
        post.setId(id);
        return post;
    }

    public List<String> getTagList() {
        if (tags == null || tags.isEmpty()) {
            return Collections.emptyList();
        }
        return Arrays.stream(tags.split(","))
                                    .map(String::trim)
                                    .filter(tag -> !tag.isEmpty())
                                    .distinct()
                                    .toList();
    }

    public String convertTagsToString(Set<Tag> tagsSet) {
        if (tagsSet == null || tagsSet.isEmpty()) {
            return "";
        }
        return tagsSet.stream()
                .map(Tag::getName)
                .filter(Objects::nonNull)
                .collect(Collectors.joining(", "));
    }
}
