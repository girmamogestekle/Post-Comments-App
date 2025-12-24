package com.sample.projects.postandcomments.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.ai.chat.model.StreamingChatModel;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Profile;
import reactor.core.publisher.Flux;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@Slf4j
@TestConfiguration
@Profile("test")
public class TestAiConfig {

    @Bean
    @Primary
    public ChatClient chatClient() {
        log.info("Creating mock ChatClient for tests");
        // Create a mock ChatModel
        ChatModel chatModel = mock(ChatModel.class);
        ChatResponse mockResponse = mock(ChatResponse.class);
        when(chatModel.call(any(Prompt.class))).thenReturn(mockResponse);
        
        StreamingChatModel streamingChatModel = mock(StreamingChatModel.class);
        when(streamingChatModel.stream(any(Prompt.class))).thenReturn(Flux.empty());
        
        // Build ChatClient with the mock ChatModel
        return ChatClient.builder(chatModel).build();
    }
}

