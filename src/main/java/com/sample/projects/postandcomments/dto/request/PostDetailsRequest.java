package com.sample.projects.postandcomments.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PostDetailsRequest {
    
    @NotNull(message = "Post ID is required")
    private Long postId;
    
    private String description;
}

