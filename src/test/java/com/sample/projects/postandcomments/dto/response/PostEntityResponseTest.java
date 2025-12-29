package com.sample.projects.postandcomments.dto.response;

import com.sample.projects.postandcomments.entity.PostDetailEntity;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

@Slf4j
@DisplayName("PostResponse DTO Tests")
class PostEntityResponseTest {

    private PostResponse postResponse;
    private PostDetailEntity postDetailEntity;
    private PostDetailResponse postDetailResponse;
    private PostCommentResponse commentResponse1;
    private PostCommentResponse commentResponse2;
    private TagResponse tagResponse1;
    private TagResponse tagResponse2;

    @BeforeEach
    void setUp() {
        postDetailResponse = PostDetailResponse.builder()
                .id(1L)
//                .postId(1L)
                .description("Test description")
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();

        commentResponse1 = PostCommentResponse.builder()
                .id(1L)
                .review("Great postEntity!")
                .postId(1L)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();

        commentResponse2 = PostCommentResponse.builder()
                .id(2L)
                .review("Very informative")
                .postId(1L)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();

        tagResponse1 = TagResponse.builder()
                .id(1L)
                .name("Spring Boot")
                .build();

        tagResponse2 = TagResponse.builder()
                .id(2L)
                .name("Java")
                .build();
    }

    @Test
    @DisplayName("Builder - Should create PostResponse with all fields")
    void testBuilder() {
        // Given
        LocalDateTime now = LocalDateTime.now();
        List<PostCommentResponse> comments = List.of(commentResponse1, commentResponse2);
        Set<TagResponse> tags = new LinkedHashSet<>(Set.of(tagResponse1, tagResponse2));

        // When
        PostResponse response = PostResponse.builder()
                .id(1L)
                .title("Test PostEntity")
                .postDetailResponse(postDetailResponse)
                .comments(comments)
                .tags(tags)
                .createdAt(now)
                .updatedAt(now)
                .build();

        // Then
        assertThat(response.getId()).isEqualTo(1L);
        assertThat(response.getTitle()).isEqualTo("Test PostEntity");
        assertThat(response.getPostDetailResponse()).isEqualTo(postDetailResponse);
        assertThat(response.getComments()).isEqualTo(comments);
        assertThat(response.getTags()).isEqualTo(tags);
        assertThat(response.getCreatedAt()).isEqualTo(now);
        assertThat(response.getUpdatedAt()).isEqualTo(now);
    }

    @Test
    @DisplayName("Builder - Should create PostResponse with minimal fields")
    void testBuilder_Minimal() {
        // When
        PostResponse response = PostResponse.builder()
                .id(1L)
                .title("Test PostEntity")
                .build();

        // Then
        assertThat(response.getId()).isEqualTo(1L);
        assertThat(response.getTitle()).isEqualTo("Test PostEntity");
        assertThat(response.getPostDetailResponse()).isNull();
        assertThat(response.getComments()).isNull();
        assertThat(response.getTags()).isNull();
        assertThat(response.getCreatedAt()).isNull();
        assertThat(response.getUpdatedAt()).isNull();
    }

    @Test
    @DisplayName("NoArgsConstructor - Should create empty PostResponse")
    void testNoArgsConstructor() {
        // When
        PostResponse response = new PostResponse();

        // Then
        assertThat(response).isNotNull();
        assertThat(response.getId()).isNull();
        assertThat(response.getTitle()).isNull();
        assertThat(response.getPostDetailResponse()).isNull();
        assertThat(response.getComments()).isNull();
        assertThat(response.getTags()).isNull();
    }

    @Test
    @DisplayName("AllArgsConstructor - Should create PostResponse with all parameters")
    void testAllArgsConstructor() {
        // Given
        LocalDateTime now = LocalDateTime.now();
        List<PostCommentResponse> comments = new ArrayList<>(List.of(commentResponse1));
        Set<TagResponse> tags = new LinkedHashSet<>(Set.of(tagResponse1));

        // When
        PostResponse response = new PostResponse(
                1L,
                "Test PostEntity",
                postDetailResponse,
                comments,
                tags,
                now,
                now
        );

        // Then
        assertThat(response.getId()).isEqualTo(1L);
        assertThat(response.getTitle()).isEqualTo("Test PostEntity");
        assertThat(response.getPostDetailResponse()).isEqualTo(postDetailResponse);
        assertThat(response.getComments()).isEqualTo(comments);
        assertThat(response.getTags()).isEqualTo(tags);
        assertThat(response.getCreatedAt()).isEqualTo(now);
        assertThat(response.getUpdatedAt()).isEqualTo(now);
    }

    @Test
    @DisplayName("Getters and Setters - Should work correctly")
    void testGettersAndSetters() {
        // Given
        PostResponse response = new PostResponse();
        LocalDateTime now = LocalDateTime.now();
        List<PostCommentResponse> comments = new ArrayList<>(List.of(commentResponse1, commentResponse2));
        Set<TagResponse> tags = new LinkedHashSet<>(Set.of(tagResponse1, tagResponse2));

        // When
        response.setId(1L);
        response.setTitle("Test PostEntity");
        response.setPostDetailResponse(postDetailResponse);
        response.setComments(comments);
        response.setTags(tags);
        response.setCreatedAt(now);
        response.setUpdatedAt(now);

        // Then
        assertThat(response.getId()).isEqualTo(1L);
        assertThat(response.getTitle()).isEqualTo("Test PostEntity");
        assertThat(response.getPostDetailResponse()).isEqualTo(postDetailResponse);
        assertThat(response.getComments()).isEqualTo(comments);
        assertThat(response.getTags()).isEqualTo(tags);
        assertThat(response.getCreatedAt()).isEqualTo(now);
        assertThat(response.getUpdatedAt()).isEqualTo(now);
    }

    @Test
    @DisplayName("PostResponse with PostDetailEntity - Should set and get correctly")
    void testPostDetails() {
        // Given
        PostResponse response = PostResponse.builder()
                .id(1L)
                .title("Test PostEntity")
                .postDetailResponse(postDetailResponse)
                .build();

        // Then
        assertThat(response.getPostDetailResponse()).isNotNull();
        assertThat(response.getPostDetailResponse().getId()).isEqualTo(1L);
        assertThat(response.getPostDetailResponse().getDescription()).isEqualTo("Test description");
    }

    @Test
    @DisplayName("PostResponse with Comments - Should set and get correctly")
    void testComments() {
        // Given
        List<PostCommentResponse> comments = List.of(commentResponse1, commentResponse2);
        PostResponse response = PostResponse.builder()
                .id(1L)
                .title("Test PostEntity")
                .comments(comments)
                .build();

        // Then
        assertThat(response.getComments()).hasSize(2);
        assertThat(response.getComments()).contains(commentResponse1, commentResponse2);
    }

    @Test
    @DisplayName("PostResponse with Tags - Should set and get correctly")
    void testTags() {
        // Given
        Set<TagResponse> tags = new LinkedHashSet<>(Set.of(tagResponse1, tagResponse2));
        PostResponse response = PostResponse.builder()
                .id(1L)
                .title("Test PostEntity")
                .tags(tags)
                .build();

        // Then
        assertThat(response.getTags()).hasSize(2);
        assertThat(response.getTags()).contains(tagResponse1, tagResponse2);
    }

    @Test
    @DisplayName("PostResponse with empty collections - Should handle null and empty")
    void testEmptyCollections() {
        // Given
        PostResponse response = PostResponse.builder()
                .id(1L)
                .title("Test PostEntity")
                .comments(new ArrayList<>())
                .tags(new LinkedHashSet<>())
                .build();

        // Then
        assertThat(response.getComments()).isEmpty();
        assertThat(response.getTags()).isEmpty();
    }

    @Test
    @DisplayName("PostResponse with timestamps - Should set and get correctly")
    void testTimestamps() {
        // Given
        LocalDateTime createdAt = LocalDateTime.of(2025, 1, 1, 10, 0, 0);
        LocalDateTime updatedAt = LocalDateTime.of(2025, 1, 2, 15, 30, 0);

        PostResponse response = PostResponse.builder()
                .id(1L)
                .title("Test PostEntity")
                .createdAt(createdAt)
                .updatedAt(updatedAt)
                .build();

        // Then
        assertThat(response.getCreatedAt()).isEqualTo(createdAt);
        assertThat(response.getUpdatedAt()).isEqualTo(updatedAt);
    }

    @Test
    @DisplayName("PostResponse - Should handle null values")
    void testNullValues() {
        // When
        PostResponse response = PostResponse.builder()
                .id(1L)
                .title("Test PostEntity")
                .postDetailResponse(null)
                .comments(null)
                .tags(null)
                .createdAt(null)
                .updatedAt(null)
                .build();

        // Then
        assertThat(response.getPostDetailResponse()).isNull();
        assertThat(response.getComments()).isNull();
        assertThat(response.getTags()).isNull();
        assertThat(response.getCreatedAt()).isNull();
        assertThat(response.getUpdatedAt()).isNull();
    }

    @Test
    @DisplayName("PostResponse - Complete example with all fields")
    void testCompleteExample() {
        // Given
        LocalDateTime now = LocalDateTime.now();
        List<PostCommentResponse> comments = List.of(commentResponse1, commentResponse2);
        Set<TagResponse> tags = new LinkedHashSet<>(Set.of(tagResponse1, tagResponse2));

        // When
        PostResponse response = PostResponse.builder()
                .id(1L)
                .title("Complete Test PostEntity")
                .postDetailResponse(postDetailResponse)
                .comments(comments)
                .tags(tags)
                .createdAt(now)
                .updatedAt(now)
                .build();

        // Then
        assertThat(response.getId()).isEqualTo(1L);
        assertThat(response.getTitle()).isEqualTo("Complete Test PostEntity");
        assertThat(response.getPostDetailResponse()).isNotNull();
        assertThat(response.getComments()).hasSize(2);
        assertThat(response.getTags()).hasSize(2);
        assertThat(response.getCreatedAt()).isNotNull();
        assertThat(response.getUpdatedAt()).isNotNull();
    }
}
