package com.sample.projects.postandcomments.repository;

import com.sample.projects.postandcomments.entity.Post;
import com.sample.projects.postandcomments.entity.PostComment;
import com.sample.projects.postandcomments.entity.PostDetails;
import com.sample.projects.postandcomments.entity.Tag;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDateTime;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@ActiveProfiles("test")
@DisplayName("PostRepository Data Layer Tests")
class PostRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private PostRepository postRepository;

    private Post post;
    private Tag tag1;
    private Tag tag2;

    @BeforeEach
    void setUp() {
        // Create tags
        tag1 = Tag.builder()
                .name("Spring Boot")
                .build();
        tag1 = entityManager.persistAndFlush(tag1);

        tag2 = Tag.builder()
                .name("Java")
                .build();
        tag2 = entityManager.persistAndFlush(tag2);

        // Create post
        post = Post.builder()
                .title("Test Post")
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .tags(new LinkedHashSet<>(Set.of(tag1, tag2)))
                .build();
    }

    @Test
    @DisplayName("save - Should save post successfully")
    void testSave() {
        // When
        Post savedPost = postRepository.save(post);

        // Then
        assertThat(savedPost.getId()).isNotNull();
        assertThat(savedPost.getTitle()).isEqualTo("Test Post");
        assertThat(savedPost.getCreatedAt()).isNotNull();
        assertThat(savedPost.getUpdatedAt()).isNotNull();
    }

    @Test
    @DisplayName("save - Should save post with tags")
    void testSave_WithTags() {
        // When
        Post savedPost = postRepository.save(post);
        entityManager.flush();
        entityManager.clear();

        // Then
        Post foundPost = entityManager.find(Post.class, savedPost.getId());
        assertThat(foundPost).isNotNull();
        assertThat(foundPost.getTags()).hasSize(2);
        assertThat(foundPost.getTags()).extracting(Tag::getName)
                .containsExactlyInAnyOrder("Spring Boot", "Java");
    }

    @Test
    @DisplayName("findById - Should find post by id")
    void testFindById() {
        // Given
        Post savedPost = entityManager.persistAndFlush(post);

        // When
        Optional<Post> foundPost = postRepository.findById(savedPost.getId());

        // Then
        assertThat(foundPost).isPresent();
        assertThat(foundPost.get().getId()).isEqualTo(savedPost.getId());
        assertThat(foundPost.get().getTitle()).isEqualTo("Test Post");
    }

    @Test
    @DisplayName("findById - Should return empty when post not found")
    void testFindById_NotFound() {
        // When
        Optional<Post> foundPost = postRepository.findById(999L);

        // Then
        assertThat(foundPost).isEmpty();
    }

    @Test
    @DisplayName("findAll - Should return all posts")
    void testFindAll() {
        // Given
        Post post1 = Post.builder()
                .title("First Post")
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();

        Post post2 = Post.builder()
                .title("Second Post")
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();

        entityManager.persistAndFlush(post1);
        entityManager.persistAndFlush(post2);

        // When
        List<Post> posts = postRepository.findAll();

        // Then
        assertThat(posts).hasSizeGreaterThanOrEqualTo(2);
        assertThat(posts).extracting(Post::getTitle)
                .contains("First Post", "Second Post");
    }

    @Test
    @DisplayName("save - Should persist post with one-to-one relationship (PostDetails)")
    void testSave_WithPostDetails() {
        // Given
        Post savedPost = entityManager.persistAndFlush(post);

        PostDetails postDetails = PostDetails.builder()
                .post(savedPost)
                .description("Test description")
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();

        savedPost.setDetails(postDetails);
        entityManager.persistAndFlush(postDetails);
        entityManager.flush();
        entityManager.clear();

        // When
        Post foundPost = entityManager.find(Post.class, savedPost.getId());

        // Then
        assertThat(foundPost).isNotNull();
        assertThat(foundPost.getPostDetails()).isNotNull();
        assertThat(foundPost.getPostDetails().getDescription()).isEqualTo("Test description");
    }

    @Test
    @DisplayName("save - Should persist post with one-to-many relationship (Comments)")
    void testSave_WithComments() {
        // Given
        Post savedPost = entityManager.persistAndFlush(post);

        PostComment comment1 = PostComment.builder()
                .review("Great post!")
                .post(savedPost)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();

        PostComment comment2 = PostComment.builder()
                .review("Very informative")
                .post(savedPost)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();

        savedPost.addComment(comment1);
        savedPost.addComment(comment2);
        entityManager.persistAndFlush(comment1);
        entityManager.persistAndFlush(comment2);
        entityManager.flush();
        entityManager.clear();

        // When
        Post foundPost = entityManager.find(Post.class, savedPost.getId());

        // Then
        assertThat(foundPost).isNotNull();
        assertThat(foundPost.getComments()).hasSize(2);
        assertThat(foundPost.getComments()).extracting(PostComment::getReview)
                .containsExactlyInAnyOrder("Great post!", "Very informative");
    }

    @Test
    @DisplayName("deleteById - Should delete post and cascade delete comments")
    void testDelete_CascadeDeleteComments() {
        // Given
        Post savedPost = entityManager.persistAndFlush(post);

        PostComment comment = PostComment.builder()
                .review("Test comment")
                .post(savedPost)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();

        savedPost.addComment(comment);
        entityManager.persistAndFlush(comment);
        Long commentId = comment.getId();
        entityManager.flush();
        entityManager.clear();

        // When
        postRepository.deleteById(savedPost.getId());
        entityManager.flush();
        entityManager.clear();

        // Then
        Post foundPost = entityManager.find(Post.class, savedPost.getId());
        PostComment foundComment = entityManager.find(PostComment.class, commentId);
        assertThat(foundPost).isNull();
        assertThat(foundComment).isNull(); // Cascade delete should remove comment
    }

    @Test
    @DisplayName("existsById - Should return true when post exists")
    void testExistsById_Exists() {
        // Given
        Post savedPost = entityManager.persistAndFlush(post);

        // When
        boolean exists = postRepository.existsById(savedPost.getId());

        // Then
        assertThat(exists).isTrue();
    }

    @Test
    @DisplayName("existsById - Should return false when post not exists")
    void testExistsById_NotExists() {
        // When
        boolean exists = postRepository.existsById(999L);

        // Then
        assertThat(exists).isFalse();
    }

    @Test
    @DisplayName("save - Should update post successfully")
    void testUpdate() {
        // Given
        Post savedPost = entityManager.persistAndFlush(post);
        String originalTitle = savedPost.getTitle();

        // When
        savedPost.setTitle("Updated Title");
        savedPost.setUpdatedAt(LocalDateTime.now());
        Post updatedPost = postRepository.save(savedPost);
        entityManager.flush();
        entityManager.clear();

        // Then
        Post foundPost = entityManager.find(Post.class, updatedPost.getId());
        assertThat(foundPost).isNotNull();
        assertThat(foundPost.getTitle()).isEqualTo("Updated Title");
        assertThat(foundPost.getTitle()).isNotEqualTo(originalTitle);
    }

    @Test
    @DisplayName("save - Should maintain many-to-many relationship with tags")
    void testManyToMany_WithTags() {
        // Given
        Post savedPost = entityManager.persistAndFlush(post);
        entityManager.flush();
        entityManager.clear();

        // When
        Post foundPost = entityManager.find(Post.class, savedPost.getId());
        Tag foundTag1 = entityManager.find(Tag.class, tag1.getId());
        Tag foundTag2 = entityManager.find(Tag.class, tag2.getId());

        // Then
        assertThat(foundPost).isNotNull();
        assertThat(foundPost.getTags()).hasSize(2);
        assertThat(foundTag1.getPosts()).contains(foundPost);
        assertThat(foundTag2.getPosts()).contains(foundPost);
    }
}

