package com.sample.projects.postandcomments.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Slf4j
@Configuration
public class AiConfig {

    // Spring AI auto-configures ChatClient.Builder for you
    @Bean
    public ChatClient chatClient(ChatClient.Builder builder) {
        log.info("Initializing ChatClient bean for Spring AI");
        ChatClient client = builder.build();
        log.debug("ChatClient bean created successfully");
        return client;
    }

}
