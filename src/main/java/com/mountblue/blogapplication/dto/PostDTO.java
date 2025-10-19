package com.mountblue.blogapplication.dto;

import com.mountblue.blogapplication.model.Post;
import lombok.Data;
import java.util.List;
import java.util.stream.Collectors;

@Data
public class PostDTO {
    private Long id;
    private String title;
    private String excerpt;
    private Boolean isPublished;
    private UserDTO author;
    private List<TagDTO> tags;

    public static PostDTO convertPostDTO(Post post) {
        if (post == null) return null;
        PostDTO dto = new PostDTO();
        dto.setId(post.getId());
        dto.setTitle(post.getTitle());
        dto.setExcerpt(post.getExcerpt());
        dto.setIsPublished(post.getIsPublished());
        dto.setAuthor(UserDTO.convertUserDTO(post.getAuthor()));
        if (post.getTags() != null) {
            dto.setTags(post.getTags().stream().map(TagDTO::convertTagDTO).collect(Collectors.toList()));
        }
        return dto;
    }
}
