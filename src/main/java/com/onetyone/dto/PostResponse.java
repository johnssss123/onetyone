package com.onetyone.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class PostResponse {
    private Long id;
    private String title;
    private String content;
    private String username;
    private LocalDateTime createdAt;
}