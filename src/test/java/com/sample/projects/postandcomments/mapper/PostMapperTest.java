package com.sample.projects.postandcomments.mapper;

import com.sample.projects.postandcomments.dto.request.PostRequest;
import com.sample.projects.postandcomments.dto.response.PostCommentResponse;
import com.sample.projects.postandcomments.dto.response.PostResponse;
import com.sample.projects.postandcomments.dto.response.TagResponse;
import com.sample.projects.postandcomments.entity.PostCommentsEntity;
import com.sample.projects.postandcomments.entity.PostDetailEntity;
import com.sample.projects.postandcomments.entity.PostEntity;
import com.sample.projects.postandcomments.entity.TagEntity;
import com.sample.projects.postandcomments.repository.PostRepository;
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
@DisplayName("PostMapper Unit Tests")
class PostMapperTest {

    private PostRepository postRepository;
    private PostMapper postMapper;

    @BeforeEach
    void setUp() {
        PostDetailMapper postDetailMapper = new PostDetailMapper(postRepository);
        postMapper = new PostMapper(postDetailMapper);
    }

    @Test
    @DisplayName("toEntity - Should convert PostRequest to PostEntity entity")
    void testToEntity() {
        // Given
        PostRequest request = PostRequest.builder()
                .title("Test PostEntity")
                .tagIds(Set.of(1L, 2L))
                .build();

        // When
        PostEntity postEntity = postMapper.toEntity(request);

        // Then
        assertThat(postEntity).isNotNull();
        assertThat(postEntity.getTitle()).isEqualTo("Test PostEntity");
        assertThat(postEntity.getId()).isNull(); // ID should not be set from request
    }

    @Test
    @DisplayName("toResponse - Should convert PostEntity entity to PostResponse")
    void testToResponse_SimplePost() {
        // Given
        PostEntity postEntity = PostEntity.builder()
                .id(1L)
                .title("Test PostEntity")
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();

        // When
        PostResponse response = postMapper.toPostResponse(postEntity);

        // Then
        assertThat(response).isNotNull();
        assertThat(response.getId()).isEqualTo(1L);
        assertThat(response.getTitle()).isEqualTo("Test PostEntity");
        assertThat(response.getCreatedAt()).isNotNull();
        assertThat(response.getUpdatedAt()).isNotNull();
    }

    @Test
    @DisplayName("toResponse - Should return null when postEntity is null")
    void testToResponse_NullPost() {
        // When
        PostResponse response = postMapper.toPostResponse(null);

        // Then
        assertThat(response).isNull();
    }

    @Test
    @DisplayName("toResponse - Should map postEntity with PostDetailEntity")
    void testToResponse_WithPostDetails() {
        // Given
        PostEntity postEntity = PostEntity.builder()
                .id(1L)
                .title("Test PostEntity")
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();

        PostDetailEntity postDetailsEntity = PostDetailEntity.builder()
                .id(1L)
                .postEntity(postEntity)
                .description("Test description")
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();

        postEntity.setDetails(postDetailsEntity);

        // When
        PostResponse response = postMapper.toPostResponse(postEntity);

        // Then
        assertThat(response).isNotNull();
        assertThat(response.getPostDetailResponse()).isNotNull();
        assertThat(response.getPostDetailResponse().getId()).isEqualTo(1L);
        assertThat(response.getPostDetailResponse().getDescription()).isEqualTo("Test description");
//        assertThat(response.getPostDetails().getPostId()).isEqualTo(1L);
    }

    @Test
    @DisplayName("toResponse - Should map postEntity with comments")
    void testToResponse_WithComments() {
        // Given
        PostEntity postEntity = PostEntity.builder()
                .id(1L)
                .title("Test PostEntity")
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .comments(new ArrayList<>())
                .build();

        PostCommentsEntity comment1 = PostCommentsEntity.builder()
                .id(1L)
                .comment("Great postEntity!")
                .postEntity(postEntity)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();

        PostCommentsEntity comment2 = PostCommentsEntity.builder()
                .id(2L)
                .comment("Very informative")
                .postEntity(postEntity)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();

        postEntity.addComment(comment1);
        postEntity.addComment(comment2);

        // When
        PostResponse response = postMapper.toPostResponse(postEntity);

        // Then
        assertThat(response).isNotNull();
        assertThat(response.getComments()).hasSize(2);
        assertThat(response.getComments()).extracting(PostCommentResponse::getReview)
                .containsExactlyInAnyOrder("Great postEntity!", "Very informative");
        assertThat(response.getComments()).extracting(PostCommentResponse::getPostId)
                .containsOnly(1L);
    }

    @Test
    @DisplayName("toResponse - Should map postEntity with tagEntities")
    void testToResponse_WithTags() {
        // Given
        PostEntity postEntity = PostEntity.builder()
                .id(1L)
                .title("Test PostEntity")
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .tagEntities(new LinkedHashSet<>())
                .build();

        TagEntity tagEntity1 = TagEntity.builder()
                .id(1L)
                .name("Spring Boot")
                .build();

        TagEntity tagEntity2 = TagEntity.builder()
                .id(2L)
                .name("Java")
                .build();

        postEntity.addTag(tagEntity1);
        postEntity.addTag(tagEntity2);

        // When
        PostResponse response = postMapper.toPostResponse(postEntity);

        // Then
        assertThat(response).isNotNull();
        assertThat(response.getTags()).hasSize(2);
        assertThat(response.getTags()).extracting(TagResponse::getName)
                .containsExactlyInAnyOrder("Spring Boot", "Java");
    }

    @Test
    @DisplayName("toResponse - Should map complete postEntity with all relationships")
    void testToResponse_CompletePost() {
        // Given
        PostEntity postEntity = PostEntity.builder()
                .id(1L)
                .title("Test PostEntity")
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .comments(new ArrayList<>())
                .tagEntities(new LinkedHashSet<>())
                .build();

        PostDetailEntity postDetailsEntity = PostDetailEntity.builder()
                .id(1L)
                .postEntity(postEntity)
                .description("Test description")
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();

        PostCommentsEntity comment = PostCommentsEntity.builder()
                .id(1L)
                .comment("Great postEntity!")
                .postEntity(postEntity)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();

        TagEntity tagEntity = TagEntity.builder()
                .id(1L)
                .name("Spring Boot")
                .build();

        postEntity.setDetails(postDetailsEntity);
        postEntity.addComment(comment);
        postEntity.addTag(tagEntity);

        // When
        PostResponse response = postMapper.toPostResponse(postEntity);

        // Then
        assertThat(response).isNotNull();
        assertThat(response.getId()).isEqualTo(1L);
        assertThat(response.getTitle()).isEqualTo("Test PostEntity");
        assertThat(response.getPostDetailResponse()).isNotNull();
        assertThat(response.getComments()).hasSize(1);
        assertThat(response.getTags()).hasSize(1);
    }

    @Test
    @DisplayName("toResponseList - Should convert list of postEntities to list of responses")
    void testToResponseList() {
        // Given
        PostEntity postEntity1 = PostEntity.builder()
                .id(1L)
                .title("First PostEntity")
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();

        PostEntity postEntity2 = PostEntity.builder()
                .id(2L)
                .title("Second PostEntity")
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();

        List<PostEntity> postEntities = List.of(postEntity1, postEntity2);

        // When
        List<PostResponse> responses = postMapper.toResponseList(postEntities);

        // Then
        assertThat(responses).hasSize(2);
        assertThat(responses).extracting(PostResponse::getId)
                .containsExactly(1L, 2L);
        assertThat(responses).extracting(PostResponse::getTitle)
                .containsExactly("First PostEntity", "Second PostEntity");
    }

    @Test
    @DisplayName("toResponseList - Should return empty list when input is null")
    void testToResponseList_Null() {
        // When
        List<PostResponse> responses = postMapper.toResponseList(null);

        // Then
        assertThat(responses).isEmpty();
    }

    @Test
    @DisplayName("toResponseList - Should return empty list when input is empty")
    void testToResponseList_Empty() {
        // When
        List<PostResponse> responses = postMapper.toResponseList(List.of());

        // Then
        assertThat(responses).isEmpty();
    }

    @Test
    @DisplayName("toPostCommentResponse - Should convert PostCommentsEntity to PostCommentResponse")
    void testToPostCommentResponse() {
        // Given
        PostEntity postEntity = PostEntity.builder()
                .id(1L)
                .title("Test PostEntity")
                .build();

        PostCommentsEntity comment = PostCommentsEntity.builder()
                .id(1L)
                .comment("Great postEntity!")
                .postEntity(postEntity)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();

        // When
        PostCommentResponse response = postMapper.toPostCommentResponse(comment);

        // Then
        assertThat(response).isNotNull();
        assertThat(response.getId()).isEqualTo(1L);
        assertThat(response.getReview()).isEqualTo("Great postEntity!");
        assertThat(response.getPostId()).isEqualTo(1L);
    }

    @Test
    @DisplayName("toTagResponse - Should convert TagEntity to TagResponse")
    void testToTagResponse() {
        // Given
        TagEntity tagEntity = TagEntity.builder()
                .id(1L)
                .name("Spring Boot")
                .build();

        // When
        TagResponse response = postMapper.toTagResponse(tagEntity);

        // Then
        assertThat(response).isNotNull();
        assertThat(response.getId()).isEqualTo(1L);
        assertThat(response.getName()).isEqualTo("Spring Boot");
    }
}