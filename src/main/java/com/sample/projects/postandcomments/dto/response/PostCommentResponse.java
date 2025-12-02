package com.sample.projects.postandcomments.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PostCommentResponse {
    
    private Long id;
    
    private String review;
    
    private Long postId;
    
    private LocalDateTime createdAt;
    
    private LocalDateTime updatedAt;
}

