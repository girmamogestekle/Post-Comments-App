package com.sample.projects.postandcomments.mapper;

import com.sample.projects.postandcomments.dto.request.PostRequest;
import com.sample.projects.postandcomments.dto.response.PostCommentResponse;
import com.sample.projects.postandcomments.dto.response.PostDetailsResponse;
import com.sample.projects.postandcomments.dto.response.PostResponse;
import com.sample.projects.postandcomments.dto.response.TagResponse;
import com.sample.projects.postandcomments.entity.Post;
import com.sample.projects.postandcomments.entity.PostComment;
import com.sample.projects.postandcomments.entity.PostDetails;
import com.sample.projects.postandcomments.entity.Tag;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("PostMapper Unit Tests")
class PostMapperTest {

    private PostMapper postMapper;

    @BeforeEach
    void setUp() {
        postMapper = new PostMapper();
    }

    @Test
    @DisplayName("toEntity - Should convert PostRequest to Post entity")
    void testToEntity() {
        // Given
        PostRequest request = PostRequest.builder()
                .title("Test Post")
                .tagIds(Set.of(1L, 2L))
                .build();

        // When
        Post post = postMapper.toEntity(request);

        // Then
        assertThat(post).isNotNull();
        assertThat(post.getTitle()).isEqualTo("Test Post");
        assertThat(post.getId()).isNull(); // ID should not be set from request
    }

    @Test
    @DisplayName("toResponse - Should convert Post entity to PostResponse")
    void testToResponse_SimplePost() {
        // Given
        Post post = Post.builder()
                .id(1L)
                .title("Test Post")
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();

        // When
        PostResponse response = postMapper.toResponse(post);

        // Then
        assertThat(response).isNotNull();
        assertThat(response.getId()).isEqualTo(1L);
        assertThat(response.getTitle()).isEqualTo("Test Post");
        assertThat(response.getCreatedAt()).isNotNull();
        assertThat(response.getUpdatedAt()).isNotNull();
    }

    @Test
    @DisplayName("toResponse - Should return null when post is null")
    void testToResponse_NullPost() {
        // When
        PostResponse response = postMapper.toResponse(null);

        // Then
        assertThat(response).isNull();
    }

    @Test
    @DisplayName("toResponse - Should map post with PostDetails")
    void testToResponse_WithPostDetails() {
        // Given
        Post post = Post.builder()
                .id(1L)
                .title("Test Post")
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();

        PostDetails postDetails = PostDetails.builder()
                .id(1L)
                .post(post)
                .description("Test description")
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();

        post.setDetails(postDetails);

        // When
        PostResponse response = postMapper.toResponse(post);

        // Then
        assertThat(response).isNotNull();
        assertThat(response.getPostDetails()).isNotNull();
        assertThat(response.getPostDetails().getId()).isEqualTo(1L);
        assertThat(response.getPostDetails().getDescription()).isEqualTo("Test description");
        assertThat(response.getPostDetails().getPostId()).isEqualTo(1L);
    }

    @Test
    @DisplayName("toResponse - Should map post with comments")
    void testToResponse_WithComments() {
        // Given
        Post post = Post.builder()
                .id(1L)
                .title("Test Post")
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .comments(new ArrayList<>())
                .build();

        PostComment comment1 = PostComment.builder()
                .id(1L)
                .review("Great post!")
                .post(post)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();

        PostComment comment2 = PostComment.builder()
                .id(2L)
                .review("Very informative")
                .post(post)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();

        post.addComment(comment1);
        post.addComment(comment2);

        // When
        PostResponse response = postMapper.toResponse(post);

        // Then
        assertThat(response).isNotNull();
        assertThat(response.getComments()).hasSize(2);
        assertThat(response.getComments()).extracting(PostCommentResponse::getReview)
                .containsExactlyInAnyOrder("Great post!", "Very informative");
        assertThat(response.getComments()).extracting(PostCommentResponse::getPostId)
                .containsOnly(1L);
    }

    @Test
    @DisplayName("toResponse - Should map post with tags")
    void testToResponse_WithTags() {
        // Given
        Post post = Post.builder()
                .id(1L)
                .title("Test Post")
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .tags(new LinkedHashSet<>())
                .build();

        Tag tag1 = Tag.builder()
                .id(1L)
                .name("Spring Boot")
                .build();

        Tag tag2 = Tag.builder()
                .id(2L)
                .name("Java")
                .build();

        post.addTag(tag1);
        post.addTag(tag2);

        // When
        PostResponse response = postMapper.toResponse(post);

        // Then
        assertThat(response).isNotNull();
        assertThat(response.getTags()).hasSize(2);
        assertThat(response.getTags()).extracting(TagResponse::getName)
                .containsExactlyInAnyOrder("Spring Boot", "Java");
    }

    @Test
    @DisplayName("toResponse - Should map complete post with all relationships")
    void testToResponse_CompletePost() {
        // Given
        Post post = Post.builder()
                .id(1L)
                .title("Test Post")
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .comments(new ArrayList<>())
                .tags(new LinkedHashSet<>())
                .build();

        PostDetails postDetails = PostDetails.builder()
                .id(1L)
                .post(post)
                .description("Test description")
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();

        PostComment comment = PostComment.builder()
                .id(1L)
                .review("Great post!")
                .post(post)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();

        Tag tag = Tag.builder()
                .id(1L)
                .name("Spring Boot")
                .build();

        post.setDetails(postDetails);
        post.addComment(comment);
        post.addTag(tag);

        // When
        PostResponse response = postMapper.toResponse(post);

        // Then
        assertThat(response).isNotNull();
        assertThat(response.getId()).isEqualTo(1L);
        assertThat(response.getTitle()).isEqualTo("Test Post");
        assertThat(response.getPostDetails()).isNotNull();
        assertThat(response.getComments()).hasSize(1);
        assertThat(response.getTags()).hasSize(1);
    }

    @Test
    @DisplayName("toResponseList - Should convert list of posts to list of responses")
    void testToResponseList() {
        // Given
        Post post1 = Post.builder()
                .id(1L)
                .title("First Post")
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();

        Post post2 = Post.builder()
                .id(2L)
                .title("Second Post")
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();

        List<Post> posts = List.of(post1, post2);

        // When
        List<PostResponse> responses = postMapper.toResponseList(posts);

        // Then
        assertThat(responses).hasSize(2);
        assertThat(responses).extracting(PostResponse::getId)
                .containsExactly(1L, 2L);
        assertThat(responses).extracting(PostResponse::getTitle)
                .containsExactly("First Post", "Second Post");
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
    @DisplayName("toPostDetailsResponse - Should convert PostDetails to PostDetailsResponse")
    void testToPostDetailsResponse() {
        // Given
        Post post = Post.builder()
                .id(1L)
                .title("Test Post")
                .build();

        PostDetails postDetails = PostDetails.builder()
                .id(1L)
                .post(post)
                .description("Test description")
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();

        // When
        PostDetailsResponse response = postMapper.toPostDetailsResponse(postDetails);

        // Then
        assertThat(response).isNotNull();
        assertThat(response.getId()).isEqualTo(1L);
        assertThat(response.getPostId()).isEqualTo(1L);
        assertThat(response.getDescription()).isEqualTo("Test description");
    }

    @Test
    @DisplayName("toPostDetailsResponse - Should return null when PostDetails is null")
    void testToPostDetailsResponse_Null() {
        // When
        PostDetailsResponse response = postMapper.toPostDetailsResponse(null);

        // Then
        assertThat(response).isNull();
    }

    @Test
    @DisplayName("toPostCommentResponse - Should convert PostComment to PostCommentResponse")
    void testToPostCommentResponse() {
        // Given
        Post post = Post.builder()
                .id(1L)
                .title("Test Post")
                .build();

        PostComment comment = PostComment.builder()
                .id(1L)
                .review("Great post!")
                .post(post)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();

        // When
        PostCommentResponse response = postMapper.toPostCommentResponse(comment);

        // Then
        assertThat(response).isNotNull();
        assertThat(response.getId()).isEqualTo(1L);
        assertThat(response.getReview()).isEqualTo("Great post!");
        assertThat(response.getPostId()).isEqualTo(1L);
    }

    @Test
    @DisplayName("toTagResponse - Should convert Tag to TagResponse")
    void testToTagResponse() {
        // Given
        Tag tag = Tag.builder()
                .id(1L)
                .name("Spring Boot")
                .build();

        // When
        TagResponse response = postMapper.toTagResponse(tag);

        // Then
        assertThat(response).isNotNull();
        assertThat(response.getId()).isEqualTo(1L);
        assertThat(response.getName()).isEqualTo("Spring Boot");
    }
}