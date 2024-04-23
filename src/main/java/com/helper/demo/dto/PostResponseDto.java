package com.helper.demo.dto;

import lombok.Data;

@Data
public class PostResponseDto {
    private int userId;
    private int id;
    private String title;
    private String body;
}
