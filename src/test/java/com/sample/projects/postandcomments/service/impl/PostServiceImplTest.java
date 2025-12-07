package com.sample.projects.postandcomments.service.impl;

import com.sample.projects.postandcomments.dto.request.PostRequest;
import com.sample.projects.postandcomments.dto.response.PostResponse;
import com.sample.projects.postandcomments.entity.Post;
import com.sample.projects.postandcomments.entity.Tag;
import com.sample.projects.postandcomments.exception.ResourceNotFoundException;
import com.sample.projects.postandcomments.exception.ValidationException;
import com.sample.projects.postandcomments.mapper.PostMapper;
import com.sample.projects.postandcomments.repository.PostRepository;
import com.sample.projects.postandcomments.service.TagService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("PostServiceImpl Unit Tests")
class PostServiceImplTest {

    @Mock
    private PostRepository postRepository;

    @Mock
    private PostMapper postMapper;

    @Mock
    private TagService tagService;

    @InjectMocks
    private PostServiceImpl postService;

    private PostRequest postRequest;
    private Post post;
    private PostResponse postResponse;
    private Tag tag;

    @BeforeEach
    void setUp() {
        postRequest = PostRequest.builder()
                .title("Test Post")
                .tagIds(Set.of(1L, 2L))
                .build();

        tag = Tag.builder()
                .id(1L)
                .name("Spring Boot")
                .build();

        post = Post.builder()
                .id(1L)
                .title("Test Post")
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .tags(new LinkedHashSet<>())
                .build();

        postResponse = PostResponse.builder()
                .id(1L)
                .title("Test Post")
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();
    }

    @Test
    @DisplayName("save - Should save post successfully without tags")
    void testSave_WithoutTags() {
        // Given
        PostRequest requestWithoutTags = PostRequest.builder()
                .title("Test Post")
                .build();

        when(postMapper.toEntity(requestWithoutTags)).thenReturn(post);
        when(postRepository.save(any(Post.class))).thenReturn(post);
        when(postMapper.toResponse(post)).thenReturn(postResponse);

        // When
        PostResponse result = postService.save(requestWithoutTags);

        // Then
        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(1L);
        assertThat(result.getTitle()).isEqualTo("Test Post");
        verify(postMapper).toEntity(requestWithoutTags);
        verify(postRepository).save(any(Post.class));
        verify(postMapper).toResponse(post);
        verify(tagService, never()).findById(anyLong());
    }

    @Test
    @DisplayName("save - Should save post successfully with tags")
    void testSave_WithTags() {
        // Given
        Tag tag2 = Tag.builder()
                .id(2L)
                .name("Java")
                .build();

        when(postMapper.toEntity(postRequest)).thenReturn(post);
        when(tagService.findById(1L)).thenReturn(Optional.of(tag));
        when(tagService.findById(2L)).thenReturn(Optional.of(tag2));
        when(postRepository.save(any(Post.class))).thenReturn(post);
        when(postMapper.toResponse(post)).thenReturn(postResponse);

        // When
        PostResponse result = postService.save(postRequest);

        // Then
        assertThat(result).isNotNull();
        verify(postMapper).toEntity(postRequest);
        // tagService.findById is called twice: once in validateTagIds() and once in the stream
        verify(tagService, times(2)).findById(1L);
        verify(tagService, times(2)).findById(2L);
        verify(postRepository).save(any(Post.class));
        verify(postMapper).toResponse(post);
    }

    @Test
    @DisplayName("save - Should throw ValidationException when tag not found")
    void testSave_WithNonExistentTags() {
        // Given
        PostRequest requestWithInvalidTags = PostRequest.builder()
                .title("Test Post")
                .tagIds(Set.of(999L))
                .build();

        when(tagService.findById(999L)).thenReturn(Optional.empty());

        // When/Then
        assertThatThrownBy(() -> postService.save(requestWithInvalidTags))
                .isInstanceOf(ValidationException.class)
                .hasMessageContaining("Invalid tag ids provided");

        verify(tagService).findById(999L);
        verify(postMapper, never()).toEntity(any(PostRequest.class));
        verify(postRepository, never()).save(any(Post.class));
    }

    @Test
    @DisplayName("findById - Should return post when exists")
    void testFindById_Exists() {
        // Given
        when(postRepository.findById(1L)).thenReturn(Optional.of(post));
        when(postMapper.toResponse(post)).thenReturn(postResponse);

        // When
        Optional<PostResponse> result = postService.findById(1L);

        // Then
        assertThat(result).isPresent();
        assertThat(result.get().getId()).isEqualTo(1L);
        verify(postRepository).findById(1L);
        verify(postMapper).toResponse(post);
    }

    @Test
    @DisplayName("findById - Should return empty when post not exists")
    void testFindById_NotExists() {
        // Given
        when(postRepository.findById(999L)).thenReturn(Optional.empty());

        // When
        Optional<PostResponse> result = postService.findById(999L);

        // Then
        assertThat(result).isEmpty();
        verify(postRepository).findById(999L);
        verify(postMapper, never()).toResponse(any(Post.class));
    }

    @Test
    @DisplayName("findAll - Should return all posts")
    void testFindAll() {
        // Given
        Post post2 = Post.builder()
                .id(2L)
                .title("Second Post")
                .build();

        PostResponse response2 = PostResponse.builder()
                .id(2L)
                .title("Second Post")
                .build();

        List<Post> posts = Arrays.asList(post, post2);
        List<PostResponse> responses = Arrays.asList(postResponse, response2);

        when(postRepository.findAll()).thenReturn(posts);
        when(postMapper.toResponseList(posts)).thenReturn(responses);

        // When
        List<PostResponse> result = postService.findAll();

        // Then
        assertThat(result).hasSize(2);
        assertThat(result.get(0).getId()).isEqualTo(1L);
        assertThat(result.get(1).getId()).isEqualTo(2L);
        verify(postRepository).findAll();
        verify(postMapper).toResponseList(posts);
    }

    @Test
    @DisplayName("update - Should update post successfully")
    void testUpdate_Success() {
        // Given
        PostRequest updateRequest = PostRequest.builder()
                .title("Updated Title")
                .build();

        Post existingPost = Post.builder()
                .id(1L)
                .title("Original Title")
                .createdAt(LocalDateTime.now().minusDays(1))
                .updatedAt(LocalDateTime.now().minusDays(1))
                .build();

        Post updatedPost = Post.builder()
                .id(1L)
                .title("Updated Title")
                .createdAt(existingPost.getCreatedAt())
                .updatedAt(LocalDateTime.now())
                .build();

        PostResponse updatedResponse = PostResponse.builder()
                .id(1L)
                .title("Updated Title")
                .build();

        when(postRepository.findById(1L)).thenReturn(Optional.of(existingPost));
        when(postRepository.save(any(Post.class))).thenReturn(updatedPost);
        when(postMapper.toResponse(updatedPost)).thenReturn(updatedResponse);

        // When
        PostResponse result = postService.update(1L, updateRequest);

        // Then
        assertThat(result).isNotNull();
        assertThat(result.getTitle()).isEqualTo("Updated Title");
        verify(postRepository).findById(1L);
        verify(postRepository).save(any(Post.class));
        verify(postMapper).toResponse(updatedPost);
    }

    @Test
    @DisplayName("update - Should throw exception when post not found")
    void testUpdate_NotFound() {
        // Given
        PostRequest updateRequest = PostRequest.builder()
                .title("Updated Title")
                .build();

        when(postRepository.findById(999L)).thenReturn(Optional.empty());

        // When/Then
        assertThatThrownBy(() -> postService.update(999L, updateRequest))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining("Post with id 999 not found");

        verify(postRepository).findById(999L);
        verify(postRepository, never()).save(any(Post.class));
    }

    @Test
    @DisplayName("update - Should update tags when provided")
    void testUpdate_WithTags() {
        // Given
        PostRequest updateRequest = PostRequest.builder()
                .title("Updated Title")
                .tagIds(Set.of(1L))
                .build();

        Tag tag1 = Tag.builder().id(1L).name("Tag1").build();

        Post existingPost = Post.builder()
                .id(1L)
                .title("Original Title")
                .tags(new LinkedHashSet<>())
                .build();

        when(postRepository.findById(1L)).thenReturn(Optional.of(existingPost));
        when(tagService.findById(1L)).thenReturn(Optional.of(tag1));
        when(postRepository.save(any(Post.class))).thenReturn(existingPost);
        when(postMapper.toResponse(existingPost)).thenReturn(postResponse);

        // When
        PostResponse result = postService.update(1L, updateRequest);

        // Then
        assertThat(result).isNotNull();
        // tagService.findById is called twice: once in validateTagIds() and once in the stream
        verify(tagService, times(2)).findById(1L);
        verify(postRepository).save(any(Post.class));
    }

    @Test
    @DisplayName("deleteById - Should delete post successfully")
    void testDeleteById_Success() {
        // Given
        when(postRepository.existsById(1L)).thenReturn(true);
        doNothing().when(postRepository).deleteById(1L);

        // When
        postService.deleteById(1L);

        // Then
        verify(postRepository).existsById(1L);
        verify(postRepository).deleteById(1L);
    }

    @Test
    @DisplayName("deleteById - Should throw exception when post not found")
    void testDeleteById_NotFound() {
        // Given
        when(postRepository.existsById(999L)).thenReturn(false);

        // When/Then
        assertThatThrownBy(() -> postService.deleteById(999L))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining("Post with id 999 not found");

        verify(postRepository).existsById(999L);
        verify(postRepository, never()).deleteById(anyLong());
    }

    @Test
    @DisplayName("existsById - Should return true when post exists")
    void testExistsById_Exists() {
        // Given
        when(postRepository.existsById(1L)).thenReturn(true);

        // When
        boolean result = postService.existsById(1L);

        // Then
        assertThat(result).isTrue();
        verify(postRepository).existsById(1L);
    }

    @Test
    @DisplayName("existsById - Should return false when post not exists")
    void testExistsById_NotExists() {
        // Given
        when(postRepository.existsById(999L)).thenReturn(false);

        // When
        boolean result = postService.existsById(999L);

        // Then
        assertThat(result).isFalse();
        verify(postRepository).existsById(999L);
    }

    @Test
    @DisplayName("findById - Should throw ValidationException when id is null")
    void testFindById_NullId() {
        // When/Then
        assertThatThrownBy(() -> postService.findById(null))
                .isInstanceOf(ValidationException.class)
                .hasMessageContaining("Post id cannot be null");
    }

    @Test
    @DisplayName("update - Should throw ValidationException when id is null")
    void testUpdate_NullId() {
        // Given
        PostRequest updateRequest = PostRequest.builder()
                .title("Updated Title")
                .build();

        // When/Then
        assertThatThrownBy(() -> postService.update(null, updateRequest))
                .isInstanceOf(ValidationException.class)
                .hasMessageContaining("Post id cannot be null");
    }

    @Test
    @DisplayName("deleteById - Should throw ValidationException when id is null")
    void testDeleteById_NullId() {
        // When/Then
        assertThatThrownBy(() -> postService.deleteById(null))
                .isInstanceOf(ValidationException.class)
                .hasMessageContaining("Post id cannot be null");
    }

    @Test
    @DisplayName("existsById - Should throw ValidationException when id is null")
    void testExistsById_NullId() {
        // When/Then
        assertThatThrownBy(() -> postService.existsById(null))
                .isInstanceOf(ValidationException.class)
                .hasMessageContaining("Post id cannot be null");
    }

    @Test
    @DisplayName("save - Should throw ValidationException when tag id is null")
    void testSave_WithNullTagId() {
        // Given
        Set<Long> tagIdsWithNull = new HashSet<>();
        tagIdsWithNull.add(1L);
        tagIdsWithNull.add(null);
        
        PostRequest requestWithNullTagId = PostRequest.builder()
                .title("Test Post")
                .tagIds(tagIdsWithNull)
                .build();

        // When/Then
        assertThatThrownBy(() -> postService.save(requestWithNullTagId))
                .isInstanceOf(ValidationException.class)
                .hasMessageContaining("Invalid tag ids provided");
    }

    @Test
    @DisplayName("save - Should throw ValidationException when tag id is invalid")
    void testSave_WithInvalidTagId() {
        // Given
        PostRequest requestWithInvalidTagId = PostRequest.builder()
                .title("Test Post")
                .tagIds(Set.of(0L, -1L))
                .build();

        // When/Then
        assertThatThrownBy(() -> postService.save(requestWithInvalidTagId))
                .isInstanceOf(ValidationException.class)
                .hasMessageContaining("Invalid tag ids provided");
    }

    @Test
    @DisplayName("save - Should throw ValidationException when tag not found")
    void testSave_WithNonExistentTag() {
        // Given
        PostRequest requestWithNonExistentTag = PostRequest.builder()
                .title("Test Post")
                .tagIds(Set.of(999L))
                .build();

        when(tagService.findById(999L)).thenReturn(Optional.empty());

        // When/Then
        assertThatThrownBy(() -> postService.save(requestWithNonExistentTag))
                .isInstanceOf(ValidationException.class)
                .hasMessageContaining("Invalid tag ids provided");
    }

    @Test
    @DisplayName("update - Should throw ValidationException when tag id is invalid")
    void testUpdate_WithInvalidTagId() {
        // Given
        PostRequest updateRequest = PostRequest.builder()
                .title("Updated Title")
                .tagIds(Set.of(0L))
                .build();

        // When/Then
        // Exception is thrown in validateTagIds() before reaching repository call
        assertThatThrownBy(() -> postService.update(1L, updateRequest))
                .isInstanceOf(ValidationException.class)
                .hasMessageContaining("Invalid tag ids provided");

        verify(postRepository, never()).findById(anyLong());
        verify(postRepository, never()).save(any(Post.class));
    }
}