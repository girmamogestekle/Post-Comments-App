package com.sample.projects.postandcomments.dto;

import com.sample.projects.postandcomments.dto.response.AiResponse;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Map;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CommonResponse<T> {
    
    private int status;
    
    private String message;
    
    private T payload;

    /** 🔹 NEW: AI-specific payload */
    private AiResponse aiPayload;
    
    private boolean success;
    
    private String timestamp;
    
    private String path;
    
    private String traceId;
    
    private List<String> errors;
    
    private Map<String, Object> meta;
    
    private String apiVersion;
    
    private String correlationId;
}

