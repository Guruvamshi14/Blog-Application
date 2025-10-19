package com.mountblue.blogapplication.repository;

import com.mountblue.blogapplication.model.Post;
import com.mountblue.blogapplication.model.Tag;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

public interface PostRepository extends JpaRepository<Post, Long> {

    @Query("""
        SELECT DISTINCT p FROM Post p
        LEFT JOIN p.tags t
        WHERE (:authors IS NULL OR p.author.name IN :authors)
          AND (:tags IS NULL OR t.name IN :tags)
          AND (:search IS NULL OR :search = '' OR
               LOWER(p.title) LIKE LOWER(CONCAT('%', :search, '%')) OR
               LOWER(p.content) LIKE LOWER(CONCAT('%', :search, '%')) OR
               LOWER(p.author.name) LIKE LOWER(CONCAT('%', :search, '%')) OR
               LOWER(t.name) LIKE LOWER(CONCAT('%', :search, '%')))
          AND p.publishedAt >= COALESCE(:startPublishDate, p.publishedAt)
          AND p.publishedAt <= COALESCE(:endPublishDate, p.publishedAt)
        GROUP BY p.id
        HAVING (:tagCount = 0 OR COUNT(DISTINCT t.name) = :tagCount)
    """)
    Page<Post> findFilteredPosts(
            @Param("authors") List<String> authors,
            @Param("tags") List<String> tags,
            @Param("tagCount") long tagCount,
            @Param("search") String search,
            @Param("startPublishDate") LocalDateTime startPublishDate,
            @Param("endPublishDate") LocalDateTime endPublishDate,
            Pageable pageable
    );


    @Query("SELECT DISTINCT p.author FROM Post p WHERE p.author IS NOT NULL")
    List<String> findAllAuthors();

    @Query("SELECT DISTINCT t FROM Tag t")
    List<Tag> findAllTags();

}