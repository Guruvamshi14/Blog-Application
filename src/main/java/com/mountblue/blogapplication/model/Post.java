package com.mountblue.blogapplication.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "posts")
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString(exclude = "tags")
public class Post {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String title;

    private String excerpt;

    @Column(columnDefinition = "TEXT")
    private String content;

    @Column(nullable = false)
    private String author;

    @Column(nullable = true)
    private LocalDateTime publishedAt;

    private Boolean isPublished;

    @ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinTable(
            name = "post_tag",
            joinColumns = @JoinColumn(name = "post_id"),
            inverseJoinColumns = @JoinColumn(name = "tag_id")
    )
    private Set<Tag> tags = new HashSet<>();

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    @PrePersist
    public void prePersist() {
        LocalDateTime now = LocalDateTime.now();
        createdAt = now;
        updatedAt = now;
        if (publishedAt == null) {
            publishedAt = now.plusDays(6);
        }
        if (isPublished == null) {
            isPublished = false;
        }
    }

    @PreUpdate
    public void preUpdate() {
        updatedAt = LocalDateTime.now();
    }

    public void addTag(Tag tag) {
        tags.add(tag);
        tag.getPosts().add(this);
    }

    public void removeTag(Tag tag) {
        tags.remove(tag);
        tag.getPosts().remove(this);
    }

    public void updateFrom(Post other) {
        this.title = other.title;
        this.author = other.author;
        this.content = other.content;
        this.excerpt = other.excerpt;
        this.publishedAt = other.publishedAt;
    }

}
