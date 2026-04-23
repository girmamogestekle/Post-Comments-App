package com.sample.projects.postandcomments.mapper;

import com.sample.projects.postandcomments.dto.request.PostDetailRequest;
import com.sample.projects.postandcomments.dto.response.PostDetailResponse;
import com.sample.projects.postandcomments.entity.PostDetailEntity;
import com.sample.projects.postandcomments.entity.PostEntity;
import com.sample.projects.postandcomments.exception.ResourceNotFoundException;
import com.sample.projects.postandcomments.repository.PostRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Proxy;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicLong;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@DisplayName("Post Detail Mapper Unit Test")
class PostDetailMapperTest {

    private PostDetailMapper postDetailMapper;

    private PostEntity savedPost;
    private PostDetailRequest postDetailRequest;
    private PostDetailEntity postDetailEntity;

    @BeforeEach
    void setUp() {
        savedPost = PostEntity.builder()
                .id(1L)
                .title("Test Post")
                .build();

        postDetailRequest = PostDetailRequest.builder()
                .postId(1L)
                .description("Test post description")
                .build();

        postDetailEntity = PostDetailEntity.builder()
                .id(1L)
                .postEntity(savedPost)
                .description("Test post description")
                .createdAt(LocalDateTime.now().minusMinutes(5))
                .updatedAt(LocalDateTime.now())
                .build();

        postDetailMapper = new PostDetailMapper(createPostRepositoryProxy(Optional.of(savedPost), null));
    }

    @Test
    @DisplayName("toPostDetailEntity - Should map request to entity when post exists")
    void testToPostDetailEntity_Success() {
        PostDetailEntity result = postDetailMapper.toPostDetailEntity(postDetailRequest);

        assertThat(result).isNotNull();
        assertThat(result.getPostEntity()).isEqualTo(savedPost);
        assertThat(result.getDescription()).isEqualTo(postDetailRequest.getDescription());
        assertThat(result.getPostEntity().getId()).isEqualTo(savedPost.getId());
    }

    @Test
    @DisplayName("toPostDetailEntity - Should throw exception when post not found")
    void testToPostDetailEntity_PostNotFound() {
        postDetailMapper = new PostDetailMapper(createPostRepositoryProxy(Optional.empty(), null));

        assertThatThrownBy(() -> postDetailMapper.toPostDetailEntity(postDetailRequest))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining("Post Entity Not Found With Post Id")
                .hasMessageContaining(String.valueOf(postDetailRequest.getPostId()));
    }

    @Test
    @DisplayName("toPostDetailResponse - Should return null when entity is null")
    void testToPostDetailResponse_NullEntity() {
        PostDetailResponse result = postDetailMapper.toPostDetailResponse(null);

        assertThat(result).isNull();
    }

    @Test
    @DisplayName("toPostDetailResponse - Should map entity to response")
    void testToPostDetailResponse_Success() {
        PostDetailResponse result = postDetailMapper.toPostDetailResponse(postDetailEntity);

        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(postDetailEntity.getId());
        assertThat(result.getPostId()).isEqualTo(savedPost.getId());
        assertThat(result.getDescription()).isEqualTo(postDetailEntity.getDescription());
        assertThat(result.getCreatedAt()).isEqualTo(postDetailEntity.getCreatedAt());
        assertThat(result.getUpdatedAt()).isEqualTo(postDetailEntity.getUpdatedAt());
    }

    @Test
    @DisplayName("toPostDetailResponse - Should set postId null when postEntity is null")
    void testToPostDetailResponse_WithNullPostEntity() {
        PostDetailEntity entityWithoutPost = PostDetailEntity.builder()
                .id(2L)
                .description("No post reference")
                .createdAt(LocalDateTime.now().minusMinutes(3))
                .updatedAt(LocalDateTime.now())
                .build();

        PostDetailResponse result = postDetailMapper.toPostDetailResponse(entityWithoutPost);

        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(entityWithoutPost.getId());
        assertThat(result.getPostId()).isNull();
        assertThat(result.getDescription()).isEqualTo(entityWithoutPost.getDescription());
    }

    @Test
    @DisplayName("toPostDetailResponses - Should return null when entities list is null")
    void testToPostDetailResponses_NullList() {
        List<PostDetailResponse> result = postDetailMapper.toPostDetailResponses(null);

        assertThat(result).isNull();
    }

    @Test
    @DisplayName("toPostDetailResponses - Should map entities list to responses list")
    void testToPostDetailResponses_Success() {
        PostDetailEntity secondEntity = PostDetailEntity.builder()
                .id(2L)
                .postEntity(PostEntity.builder().id(2L).title("Second post").build())
                .description("Second description")
                .createdAt(LocalDateTime.now().minusMinutes(2))
                .updatedAt(LocalDateTime.now())
                .build();

        List<PostDetailResponse> responses = postDetailMapper.toPostDetailResponses(List.of(postDetailEntity, secondEntity));

        assertThat(responses).hasSize(2);
        assertThat(responses.get(0).getId()).isEqualTo(1L);
        assertThat(responses.get(0).getPostId()).isEqualTo(1L);
        assertThat(responses.get(1).getId()).isEqualTo(2L);
        assertThat(responses.get(1).getPostId()).isEqualTo(2L);
        assertThat(responses.get(1).getDescription()).isEqualTo("Second description");
    }

    @Test
    @DisplayName("toPostDetailEntity - Should call repository with request postId")
    void testToPostDetailEntity_UsesPostIdFromRequest() {
        AtomicLong capturedId = new AtomicLong(-1L);
        postDetailMapper = new PostDetailMapper(createPostRepositoryProxy(Optional.of(savedPost), capturedId));

        postDetailMapper.toPostDetailEntity(postDetailRequest);

        assertThat(capturedId.get()).isEqualTo(postDetailRequest.getPostId());
    }

    private PostRepository createPostRepositoryProxy(Optional<PostEntity> result, AtomicLong capturedId) {
        return (PostRepository) Proxy.newProxyInstance(
                PostRepository.class.getClassLoader(),
                new Class<?>[]{PostRepository.class},
                (proxy, method, args) -> {
                    if ("findById".equals(method.getName())) {
                        if (capturedId != null && args != null && args.length > 0 && args[0] instanceof Long id) {
                            capturedId.set(id);
                        }
                        return result;
                    }
                    if ("toString".equals(method.getName())) {
                        return "PostRepositoryProxy";
                    }
                    if ("hashCode".equals(method.getName())) {
                        return System.identityHashCode(proxy);
                    }
                    if ("equals".equals(method.getName())) {
                        return proxy == args[0];
                    }
                    throw new UnsupportedOperationException("Unsupported method in test: " + method.getName());
                }
        );
    }
}
