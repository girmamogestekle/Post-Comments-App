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
public class PostDetailRequest {
    
    @NotNull(message = "Post Id Is Required")
    private Long postId;

    @NotNull(message = "Description Is Required")
    @NotBlank(message = "Description Does Not Blank")
    private String description;
}

