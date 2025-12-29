package com.sample.projects.postandcomments.service.impl;

import com.sample.projects.postandcomments.dto.response.AiResponse;
import com.sample.projects.postandcomments.dto.response.PostResponse;
import com.sample.projects.postandcomments.service.AiService;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.stereotype.Service;

@Service
public class AiServiceImpl implements AiService {

    private final ChatClient chatClient;

    public AiServiceImpl(ChatClient chatClient) {
        this.chatClient = chatClient;
    }

    @Override
    public AiResponse explainPost(PostResponse postResponse) {
        String explanation =  chatClient
                .prompt()                         // fluent API
                .user("""
                        You are an assistant for a Posts & Comments API.
                        
                        Explain this blog/postEntity in simple, clear language (3–5 sentences).
                        Focus on the main idea, audience, and tone.

                        Title: %s
                        """.formatted(postResponse.getTitle()))
                .call()
                .content();

        return AiResponse.builder()
                .resourceType("PostEntity")
                .resourceId(postResponse.getId())
                .title(postResponse.getTitle())
                .explanation(explanation)
                .build();
    }

}
