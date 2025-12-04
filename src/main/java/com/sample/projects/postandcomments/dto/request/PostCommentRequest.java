package com.sample.projects.postandcomments.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PostCommentRequest {
    
    @NotBlank(message = "Review is required")
    private String review;
    
    @NotNull(message = "Post ID is required")
    private Long postId;
}

