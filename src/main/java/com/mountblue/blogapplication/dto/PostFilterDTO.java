package com.mountblue.blogapplication.dto;

import com.mountblue.blogapplication.model.User;
import lombok.Data;

import java.time.LocalDate;
import java.util.List;

@Data
public class PostFilterDTO {
    private List<User> authors;
    private List<String> tags;
    private String search;
    private String sortField = "publishedAt";
    private String order = "desc";
    private int page = 0;
    private int size = 3;

    private LocalDate startPublishDate;
    private LocalDate endPublishDate;
}

