package com.mountblue.blogapplication.dto;

import com.mountblue.blogapplication.model.Comment;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
public class CommentDTO {
    private Long id;
    private String name;
    private String email;
    private String comment;
    private PostDTO postDTO;

    public static CommentDTO convertCommentDTO(Comment comment) {
        if (comment == null) return null;

        CommentDTO dto = new CommentDTO();
        dto.setId(comment.getId());
        dto.setName(comment.getName());
        dto.setEmail(comment.getEmail());
        dto.setComment(comment.getComment());
        dto.setPostDTO(PostDTO.convertPostDTO(comment.getPost()));

        return dto;
    }
}
