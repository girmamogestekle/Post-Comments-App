package com.sample.projects.postandcomments.dto.response;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@Slf4j
@DisplayName("Post Detail Response DTO Tests")
public class PostDetailResponseTest {

    @Test
    @DisplayName("Builder - Should create Post Detail Response with all fields")
    void testBuilder(){
        // Given
        LocalDateTime now = LocalDateTime.now();

        // When
        PostDetailResponse postDetailResponse = PostDetailResponse.builder()
                .id(1L)
                .postId(1L)
                .description("Test Description")
                .createdAt(now)
                .updatedAt(now)
                .build();

        // Then
        assertThat(postDetailResponse.getId()).isEqualTo(1L);
        assertThat(postDetailResponse.getPostId()).isEqualTo(1L);
        assertThat(postDetailResponse.getDescription()).isEqualTo("Test Description");
        assertThat(postDetailResponse.getCreatedAt()).isEqualTo(now);
        assertThat(postDetailResponse.getUpdatedAt()).isEqualTo(now);
    }

    @Test
    @DisplayName("Builder - Should create Post Detail Response with minimal fields")
    void testBuilder_Minimal(){
        // Given
        LocalDateTime now = LocalDateTime.now();

        // When
        PostDetailResponse postDetailResponse = PostDetailResponse.builder()
                .id(1L)
                .postId(1L)
                .description("Test Description")
                .createdAt(now)
                .build();

        // Then
        assertThat(postDetailResponse.getId()).isEqualTo(1L);
        assertThat(postDetailResponse.getPostId()).isEqualTo(1L);
        assertThat(postDetailResponse.getDescription()).isEqualTo("Test Description");
        assertThat(postDetailResponse.getCreatedAt()).isEqualTo(now);
        assertThat(postDetailResponse.getUpdatedAt()).isNull();
    }

    @Test
    @DisplayName("No Args Constructor - Should create empty post detail response")
    void testNoArgsConstructor(){
        // When
        PostDetailResponse postDetailResponse = new PostDetailResponse();

        // Then
        assertThat(postDetailResponse.getId()).isNull();
        assertThat(postDetailResponse.getPostId()).isNull();
        assertThat(postDetailResponse.getDescription()).isNull();
        assertThat(postDetailResponse.getCreatedAt()).isNull();
        assertThat(postDetailResponse.getUpdatedAt()).isNull();
    }

    @Test
    @DisplayName("All Args Constructor - Should create post detail response with all parameters")
    void testAllArgsConstructor(){
        // Given
        LocalDateTime now = LocalDateTime.now();

        // When
        PostDetailResponse postDetailResponse = new PostDetailResponse(
                1L,
                1L,
                "Test Description",
                now,
                now
        );

        // Then
        assertThat(postDetailResponse.getId()).isEqualTo(1L);
        assertThat(postDetailResponse.getPostId()).isEqualTo(1L);
        assertThat(postDetailResponse.getDescription()).isEqualTo("Test Description");
        assertThat(postDetailResponse.getCreatedAt()).isEqualTo(now);
        assertThat(postDetailResponse.getUpdatedAt()).isEqualTo(now);
    }

    @Test
    @DisplayName("Getters And Setters - Should work correctly")
    void testGettersAndSetters(){
        // Given
        LocalDateTime now = LocalDateTime.now();
        PostDetailResponse postDetailResponse = new PostDetailResponse();

        // When
        postDetailResponse.setId(1L);
        postDetailResponse.setPostId(1L);
        postDetailResponse.setDescription("Test Description");
        postDetailResponse.setCreatedAt(now);
        postDetailResponse.setUpdatedAt(now);

        // Then
        assertThat(postDetailResponse.getId()).isEqualTo(1L);
        assertThat(postDetailResponse.getPostId()).isEqualTo(1L);
        assertThat(postDetailResponse.getDescription()).isEqualTo("Test Description");
        assertThat(postDetailResponse.getCreatedAt()).isEqualTo(now);
        assertThat(postDetailResponse.getUpdatedAt()).isEqualTo(now);
    }

    @Test
    @DisplayName("Post Detail Response With Timestamps - Should set and get correctly")
    void testTimestamps(){
        // Given
        LocalDateTime createdAt = LocalDateTime.of(2025, 1, 1, 10, 0, 0);
        LocalDateTime updatedAt = LocalDateTime.of(2025, 1, 2, 15, 30, 0);

        // When
        PostDetailResponse postDetailResponse = PostDetailResponse.builder()
                .id(1L)
                .postId(1L)
                .description("Test Description")
                .createdAt(createdAt)
                .updatedAt(updatedAt)
                .build();

        // Then
        assertThat(postDetailResponse.getCreatedAt()).isEqualTo(createdAt);
        assertThat(postDetailResponse.getUpdatedAt()).isEqualTo(updatedAt);
    }

    @Test
    @DisplayName("Post Detail Response - Should handle null values")
    void testNullValues(){
        // When
        PostDetailResponse postDetailResponse = PostDetailResponse.builder()
                .id(null)
                .postId(null)
                .description(null)
                .createdAt(null)
                .updatedAt(null)
                .build();

        // Then
        assertThat(postDetailResponse.getId()).isNull();
        assertThat(postDetailResponse.getPostId()).isNull();
        assertThat(postDetailResponse.getDescription()).isNull();
        assertThat(postDetailResponse.getCreatedAt()).isNull();
        assertThat(postDetailResponse.getUpdatedAt()).isNull();
    }

    @Test
    @DisplayName("Post Detail Response - Complete example with all fields")
    void testCompleteExamples(){
        // Given
        LocalDateTime now = LocalDateTime.now();

        // When
        PostDetailResponse postDetailResponse = PostDetailResponse.builder()
                .id(1L)
                .postId(1L)
                .description("Test Description")
                .createdAt(now)
                .updatedAt(now)
                .build();

        // Then
        assertThat(postDetailResponse.getId()).isEqualTo(1L);
        assertThat(postDetailResponse.getPostId()).isEqualTo(1L);
        assertThat(postDetailResponse.getDescription()).isEqualTo("Test Description");
        assertThat(postDetailResponse.getCreatedAt()).isEqualTo(now);
        assertThat(postDetailResponse.getUpdatedAt()).isEqualTo(now);
    }
}
