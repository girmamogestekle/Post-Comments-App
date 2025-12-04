package com.sample.projects.postandcomments.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AiResponse {

    private String resourceType;       // POST, COMMENT, TAG, POST_DETAILS
    private Long resourceId;
    private String title;              // or name/subject
    private String explanation;        // main AI text
    private List<String> suggestedTags; // optional: AI-generated tags/keywords

}
