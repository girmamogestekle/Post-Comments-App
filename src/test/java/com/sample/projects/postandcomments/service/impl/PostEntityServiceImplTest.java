package com.sample.projects.postandcomments.service.impl;

import com.sample.projects.postandcomments.dto.request.PostRequest;
import com.sample.projects.postandcomments.dto.response.PostResponse;
import com.sample.projects.postandcomments.entity.PostEntity;
import com.sample.projects.postandcomments.entity.TagEntity;
import com.sample.projects.postandcomments.exception.ResourceNotFoundException;
import com.sample.projects.postandcomments.exception.ValidationException;
import com.sample.projects.postandcomments.mapper.PostMapper;
import com.sample.projects.postandcomments.repository.PostRepository;
import com.sample.projects.postandcomments.service.TagService;
import lombok.extern.slf4j.Slf4j;
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

@Slf4j
@ExtendWith(MockitoExtension.class)
@DisplayName("PostServiceImpl Unit Tests")
class PostEntityServiceImplTest {

    @Mock
    private PostRepository postRepository;

    @Mock
    private PostMapper postMapper;

    @Mock
    private TagService tagService;

    @InjectMocks
    private PostServiceImpl postService;

    private PostRequest postRequest;
    private PostEntity postEntity;
    private PostResponse postResponse;
    private TagEntity tagEntity;

    @BeforeEach
    void setUp() {
        postRequest = PostRequest.builder()
                .title("Test PostEntity")
                .tagIds(Set.of(1L, 2L))
                .build();

        tagEntity = TagEntity.builder()
                .id(1L)
                .name("Spring Boot")
                .build();

        postEntity = PostEntity.builder()
                .id(1L)
                .title("Test PostEntity")
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .tagEntities(new LinkedHashSet<>())
                .build();

        postResponse = PostResponse.builder()
                .id(1L)
                .title("Test PostEntity")
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();
    }

    @Test
    @DisplayName("save - Should save postEntity successfully without tagEntities")
    void testSave_WithoutTags() {
        // Given
        PostRequest requestWithoutTags = PostRequest.builder()
                .title("Test PostEntity")
                .build();

        when(postMapper.toEntity(requestWithoutTags)).thenReturn(postEntity);
        when(postRepository.save(any(PostEntity.class))).thenReturn(postEntity);
        when(postMapper.toPostResponse(postEntity)).thenReturn(postResponse);

        // When
        PostResponse result = postService.save(requestWithoutTags);

        // Then
        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(1L);
        assertThat(result.getTitle()).isEqualTo("Test PostEntity");
        verify(postMapper).toEntity(requestWithoutTags);
        verify(postRepository).save(any(PostEntity.class));
        verify(postMapper).toPostResponse(postEntity);
        verify(tagService, never()).findById(anyLong());
    }

    @Test
    @DisplayName("save - Should save postEntity successfully with tagEntities")
    void testSave_WithTags() {
        // Given
        TagEntity tagEntity2 = TagEntity.builder()
                .id(2L)
                .name("Java")
                .build();

        when(postMapper.toEntity(postRequest)).thenReturn(postEntity);
        when(tagService.findById(1L)).thenReturn(Optional.of(tagEntity));
        when(tagService.findById(2L)).thenReturn(Optional.of(tagEntity2));
        when(postRepository.save(any(PostEntity.class))).thenReturn(postEntity);
        when(postMapper.toPostResponse(postEntity)).thenReturn(postResponse);

        // When
        PostResponse result = postService.save(postRequest);

        // Then
        assertThat(result).isNotNull();
        verify(postMapper).toEntity(postRequest);
        // tagService.findById is called twice: once in validateTagIds() and once in the stream
        verify(tagService, times(2)).findById(1L);
        verify(tagService, times(2)).findById(2L);
        verify(postRepository).save(any(PostEntity.class));
        verify(postMapper).toPostResponse(postEntity);
    }

    @Test
    @DisplayName("save - Should throw ValidationException when tagEntity not found")
    void testSave_WithNonExistentTags() {
        // Given
        PostRequest requestWithInvalidTags = PostRequest.builder()
                .title("Test PostEntity")
                .tagIds(Set.of(999L))
                .build();

        when(tagService.findById(999L)).thenReturn(Optional.empty());

        // When/Then
        assertThatThrownBy(() -> postService.save(requestWithInvalidTags))
                .isInstanceOf(ValidationException.class)
                .hasMessageContaining("Invalid tag ids provided");

        verify(tagService).findById(999L);
        verify(postMapper, never()).toEntity(any(PostRequest.class));
        verify(postRepository, never()).save(any(PostEntity.class));
    }

    @Test
    @DisplayName("findById - Should return postEntity when exists")
    void testFindById_Exists() {
        // Given
        when(postRepository.findById(1L)).thenReturn(Optional.of(postEntity));
        when(postMapper.toPostResponse(postEntity)).thenReturn(postResponse);

        // When
        Optional<PostResponse> result = postService.findById(1L);

        // Then
        assertThat(result).isPresent();
        assertThat(result.get().getId()).isEqualTo(1L);
        verify(postRepository).findById(1L);
        verify(postMapper).toPostResponse(postEntity);
    }

    @Test
    @DisplayName("findById - Should return empty when postEntity not exists")
    void testFindById_NotExists() {
        // Given
        when(postRepository.findById(999L)).thenReturn(Optional.empty());

        // When
        Optional<PostResponse> result = postService.findById(999L);

        // Then
        assertThat(result).isEmpty();
        verify(postRepository).findById(999L);
        verify(postMapper, never()).toPostResponse(any(PostEntity.class));
    }

    @Test
    @DisplayName("findAll - Should return all postEntities")
    void testFindAll() {
        // Given
        PostEntity postEntity2 = PostEntity.builder()
                .id(2L)
                .title("Second PostEntity")
                .build();

        PostResponse response2 = PostResponse.builder()
                .id(2L)
                .title("Second PostEntity")
                .build();

        List<PostEntity> postEntities = Arrays.asList(postEntity, postEntity2);
        List<PostResponse> responses = Arrays.asList(postResponse, response2);

        when(postRepository.findAll()).thenReturn(postEntities);
        when(postMapper.toResponseList(postEntities)).thenReturn(responses);

        // When
        List<PostResponse> result = postService.findAll();

        // Then
        assertThat(result).hasSize(2);
        assertThat(result.get(0).getId()).isEqualTo(1L);
        assertThat(result.get(1).getId()).isEqualTo(2L);
        verify(postRepository).findAll();
        verify(postMapper).toResponseList(postEntities);
    }

    @Test
    @DisplayName("update - Should update postEntity successfully")
    void testUpdate_Success() {
        // Given
        PostRequest updateRequest = PostRequest.builder()
                .title("Updated Title")
                .build();

        PostEntity existingPostEntity = PostEntity.builder()
                .id(1L)
                .title("Original Title")
                .createdAt(LocalDateTime.now().minusDays(1))
                .updatedAt(LocalDateTime.now().minusDays(1))
                .build();

        PostEntity updatedPostEntity = PostEntity.builder()
                .id(1L)
                .title("Updated Title")
                .createdAt(existingPostEntity.getCreatedAt())
                .updatedAt(LocalDateTime.now())
                .build();

        PostResponse updatedResponse = PostResponse.builder()
                .id(1L)
                .title("Updated Title")
                .build();

        when(postRepository.findById(1L)).thenReturn(Optional.of(existingPostEntity));
        when(postRepository.save(any(PostEntity.class))).thenReturn(updatedPostEntity);
        when(postMapper.toPostResponse(updatedPostEntity)).thenReturn(updatedResponse);

        // When
        PostResponse result = postService.update(1L, updateRequest);

        // Then
        assertThat(result).isNotNull();
        assertThat(result.getTitle()).isEqualTo("Updated Title");
        verify(postRepository).findById(1L);
        verify(postRepository).save(any(PostEntity.class));
        verify(postMapper).toPostResponse(updatedPostEntity);
    }

    @Test
    @DisplayName("update - Should throw exception when postEntity not found")
    void testUpdate_NotFound() {
        // Given
        PostRequest updateRequest = PostRequest.builder()
                .title("Updated Title")
                .build();

        when(postRepository.findById(999L)).thenReturn(Optional.empty());

        // When/Then
        assertThatThrownBy(() -> postService.update(999L, updateRequest))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining("PostEntity with id 999 not found");

        verify(postRepository).findById(999L);
        verify(postRepository, never()).save(any(PostEntity.class));
    }

    @Test
    @DisplayName("update - Should update tagEntities when provided")
    void testUpdate_WithTags() {
        // Given
        PostRequest updateRequest = PostRequest.builder()
                .title("Updated Title")
                .tagIds(Set.of(1L))
                .build();

        TagEntity tagEntity1 = TagEntity.builder().id(1L).name("Tag1").build();

        PostEntity existingPostEntity = PostEntity.builder()
                .id(1L)
                .title("Original Title")
                .tagEntities(new LinkedHashSet<>())
                .build();

        when(postRepository.findById(1L)).thenReturn(Optional.of(existingPostEntity));
        when(tagService.findById(1L)).thenReturn(Optional.of(tagEntity1));
        when(postRepository.save(any(PostEntity.class))).thenReturn(existingPostEntity);
        when(postMapper.toPostResponse(existingPostEntity)).thenReturn(postResponse);

        // When
        PostResponse result = postService.update(1L, updateRequest);

        // Then
        assertThat(result).isNotNull();
        // tagService.findById is called twice: once in validateTagIds() and once in the stream
        verify(tagService, times(2)).findById(1L);
        verify(postRepository).save(any(PostEntity.class));
    }

    @Test
    @DisplayName("deleteById - Should delete postEntity successfully")
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
    @DisplayName("deleteById - Should throw exception when postEntity not found")
    void testDeleteById_NotFound() {
        // Given
        when(postRepository.existsById(999L)).thenReturn(false);

        // When/Then
        assertThatThrownBy(() -> postService.deleteById(999L))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining("PostEntity with id 999 not found");

        verify(postRepository).existsById(999L);
        verify(postRepository, never()).deleteById(anyLong());
    }

    @Test
    @DisplayName("existsById - Should return true when postEntity exists")
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
    @DisplayName("existsById - Should return false when postEntity not exists")
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
                .hasMessageContaining("PostEntity id cannot be null");
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
                .hasMessageContaining("PostEntity id cannot be null");
    }

    @Test
    @DisplayName("deleteById - Should throw ValidationException when id is null")
    void testDeleteById_NullId() {
        // When/Then
        assertThatThrownBy(() -> postService.deleteById(null))
                .isInstanceOf(ValidationException.class)
                .hasMessageContaining("PostEntity id cannot be null");
    }

    @Test
    @DisplayName("existsById - Should throw ValidationException when id is null")
    void testExistsById_NullId() {
        // When/Then
        assertThatThrownBy(() -> postService.existsById(null))
                .isInstanceOf(ValidationException.class)
                .hasMessageContaining("PostEntity id cannot be null");
    }

    @Test
    @DisplayName("save - Should throw ValidationException when tagEntity id is null")
    void testSave_WithNullTagId() {
        // Given
        Set<Long> tagIdsWithNull = new HashSet<>();
        tagIdsWithNull.add(1L);
        tagIdsWithNull.add(null);
        
        PostRequest requestWithNullTagId = PostRequest.builder()
                .title("Test PostEntity")
                .tagIds(tagIdsWithNull)
                .build();

        // When/Then
        assertThatThrownBy(() -> postService.save(requestWithNullTagId))
                .isInstanceOf(ValidationException.class)
                .hasMessageContaining("Invalid tag ids provided");
    }

    @Test
    @DisplayName("save - Should throw ValidationException when tagEntity id is invalid")
    void testSave_WithInvalidTagId() {
        // Given
        PostRequest requestWithInvalidTagId = PostRequest.builder()
                .title("Test PostEntity")
                .tagIds(Set.of(0L, -1L))
                .build();

        // When/Then
        assertThatThrownBy(() -> postService.save(requestWithInvalidTagId))
                .isInstanceOf(ValidationException.class)
                .hasMessageContaining("Invalid tag ids provided");
    }

    @Test
    @DisplayName("save - Should throw ValidationException when tagEntity not found")
    void testSave_WithNonExistentTag() {
        // Given
        PostRequest requestWithNonExistentTag = PostRequest.builder()
                .title("Test PostEntity")
                .tagIds(Set.of(999L))
                .build();

        when(tagService.findById(999L)).thenReturn(Optional.empty());

        // When/Then
        assertThatThrownBy(() -> postService.save(requestWithNonExistentTag))
                .isInstanceOf(ValidationException.class)
                .hasMessageContaining("Invalid tag ids provided");
    }

    @Test
    @DisplayName("update - Should throw ValidationException when tagEntity id is invalid")
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
        verify(postRepository, never()).save(any(PostEntity.class));
    }
}