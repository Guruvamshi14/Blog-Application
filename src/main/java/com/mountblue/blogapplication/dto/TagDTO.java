package com.mountblue.blogapplication.dto;

import com.mountblue.blogapplication.model.Tag;
import lombok.Data;

@Data
public class TagDTO {
    private Long id;
    private String name;

    public static TagDTO convertTagDTO(Tag tag) {
        if (tag == null) return null;
        TagDTO dto = new TagDTO();
        dto.setId(tag.getId());
        dto.setName(tag.getName());
        return dto;
    }
}
