package com.sample.projects.postandcomments.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PostResponse {
    
    private Long id;
    
    private String title;
    
    private PostDetailsResponse postDetails;
    
    private List<PostCommentResponse> comments;
    
    private Set<TagResponse> tags;
    
    private LocalDateTime createdAt;
    
    private LocalDateTime updatedAt;
}

