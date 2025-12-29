package com.sample.projects.postandcomments.repository;

import com.sample.projects.postandcomments.entity.PostCommentsEntity;
import com.sample.projects.postandcomments.entity.PostDetailEntity;
import com.sample.projects.postandcomments.entity.PostEntity;
import com.sample.projects.postandcomments.entity.TagEntity;
import lombok.extern.slf4j.Slf4j;
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
@Slf4j
@DisplayName("PostRepository Data Layer Tests")
class PostEntityRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private PostRepository postRepository;

    private PostEntity postEntity;
    private TagEntity tagEntity1;
    private TagEntity tagEntity2;

    @BeforeEach
    void setUp() {
        // Create tagEntities
        tagEntity1 = TagEntity.builder()
                .name("Spring Boot")
                .build();
        tagEntity1 = entityManager.persistAndFlush(tagEntity1);

        tagEntity2 = TagEntity.builder()
                .name("Java")
                .build();
        tagEntity2 = entityManager.persistAndFlush(tagEntity2);

        // Create postEntity
        postEntity = PostEntity.builder()
                .title("Test PostEntity")
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .tagEntities(new LinkedHashSet<>(Set.of(tagEntity1, tagEntity2)))
                .build();
    }

    @Test
    @DisplayName("save - Should save postEntity successfully")
    void testSave() {
        // When
        PostEntity savedPostEntity = postRepository.save(postEntity);

        // Then
        assertThat(savedPostEntity.getId()).isNotNull();
        assertThat(savedPostEntity.getTitle()).isEqualTo("Test PostEntity");
        assertThat(savedPostEntity.getCreatedAt()).isNotNull();
        assertThat(savedPostEntity.getUpdatedAt()).isNotNull();
    }

    @Test
    @DisplayName("save - Should save postEntity with tagEntities")
    void testSave_WithTags() {
        // When
        PostEntity savedPostEntity = postRepository.save(postEntity);
        entityManager.flush();
        entityManager.clear();

        // Then
        PostEntity foundPostEntity = entityManager.find(PostEntity.class, savedPostEntity.getId());
        assertThat(foundPostEntity).isNotNull();
        assertThat(foundPostEntity.getTagEntities()).hasSize(2);
        assertThat(foundPostEntity.getTagEntities()).extracting(TagEntity::getName)
                .containsExactlyInAnyOrder("Spring Boot", "Java");
    }

    @Test
    @DisplayName("findById - Should find postEntity by id")
    void testFindById() {
        // Given
        PostEntity savedPostEntity = entityManager.persistAndFlush(postEntity);

        // When
        Optional<PostEntity> foundPost = postRepository.findById(savedPostEntity.getId());

        // Then
        assertThat(foundPost).isPresent();
        assertThat(foundPost.get().getId()).isEqualTo(savedPostEntity.getId());
        assertThat(foundPost.get().getTitle()).isEqualTo("Test PostEntity");
    }

    @Test
    @DisplayName("findById - Should return empty when postEntity not found")
    void testFindById_NotFound() {
        // When
        Optional<PostEntity> foundPost = postRepository.findById(999L);

        // Then
        assertThat(foundPost).isEmpty();
    }

    @Test
    @DisplayName("findAll - Should return all postEntities")
    void testFindAll() {
        // Given
        PostEntity postEntity1 = PostEntity.builder()
                .title("First PostEntity")
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();

        PostEntity postEntity2 = PostEntity.builder()
                .title("Second PostEntity")
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();

        entityManager.persistAndFlush(postEntity1);
        entityManager.persistAndFlush(postEntity2);

        // When
        List<PostEntity> postEntities = postRepository.findAll();

        // Then
        assertThat(postEntities).hasSizeGreaterThanOrEqualTo(2);
        assertThat(postEntities).extracting(PostEntity::getTitle)
                .contains("First PostEntity", "Second PostEntity");
    }

    @Test
    @DisplayName("save - Should persist postEntity with one-to-one relationship (PostDetailEntity)")
    void testSave_WithPostDetails() {
        // Given
        PostEntity savedPostEntity = entityManager.persistAndFlush(postEntity);

        PostDetailEntity postDetailsEntity = PostDetailEntity.builder()
                .postEntity(savedPostEntity)
                .description("Test description")
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();

        savedPostEntity.setDetails(postDetailsEntity);
        entityManager.persistAndFlush(postDetailsEntity);
        entityManager.flush();
        entityManager.clear();

        // When
        PostEntity foundPostEntity = entityManager.find(PostEntity.class, savedPostEntity.getId());

        // Then
        assertThat(foundPostEntity).isNotNull();
        assertThat(foundPostEntity.getPostDetailEntity()).isNotNull();
        assertThat(foundPostEntity.getPostDetailEntity().getDescription()).isEqualTo("Test description");
    }

    @Test
    @DisplayName("save - Should persist postEntity with one-to-many relationship (Comments)")
    void testSave_WithComments() {
        // Given
        PostEntity savedPostEntity = entityManager.persistAndFlush(postEntity);

        PostCommentsEntity comment1 = PostCommentsEntity.builder()
                .comment("Great postEntity!")
                .postEntity(savedPostEntity)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();

        PostCommentsEntity comment2 = PostCommentsEntity.builder()
                .comment("Very informative")
                .postEntity(savedPostEntity)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();

        savedPostEntity.addComment(comment1);
        savedPostEntity.addComment(comment2);
        entityManager.persistAndFlush(comment1);
        entityManager.persistAndFlush(comment2);
        entityManager.flush();
        entityManager.clear();

        // When
        PostEntity foundPostEntity = entityManager.find(PostEntity.class, savedPostEntity.getId());

        // Then
        assertThat(foundPostEntity).isNotNull();
        assertThat(foundPostEntity.getComments()).hasSize(2);
        assertThat(foundPostEntity.getComments()).extracting(PostCommentsEntity::getComment)
                .containsExactlyInAnyOrder("Great postEntity!", "Very informative");
    }

    @Test
    @DisplayName("deleteById - Should delete postEntity and cascade delete comments")
    void testDelete_CascadeDeleteComments() {
        // Given
        PostEntity savedPostEntity = entityManager.persistAndFlush(postEntity);

        PostCommentsEntity comment = PostCommentsEntity.builder()
                .comment("Test comment")
                .postEntity(savedPostEntity)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();

        savedPostEntity.addComment(comment);
        entityManager.persistAndFlush(comment);
        Long commentId = comment.getId();
        entityManager.flush();
        entityManager.clear();

        // When
        postRepository.deleteById(savedPostEntity.getId());
        entityManager.flush();
        entityManager.clear();

        // Then
        PostEntity foundPostEntity = entityManager.find(PostEntity.class, savedPostEntity.getId());
        PostCommentsEntity foundComment = entityManager.find(PostCommentsEntity.class, commentId);
        assertThat(foundPostEntity).isNull();
        assertThat(foundComment).isNull(); // Cascade delete should remove comment
    }

    @Test
    @DisplayName("existsById - Should return true when postEntity exists")
    void testExistsById_Exists() {
        // Given
        PostEntity savedPostEntity = entityManager.persistAndFlush(postEntity);

        // When
        boolean exists = postRepository.existsById(savedPostEntity.getId());

        // Then
        assertThat(exists).isTrue();
    }

    @Test
    @DisplayName("existsById - Should return false when postEntity not exists")
    void testExistsById_NotExists() {
        // When
        boolean exists = postRepository.existsById(999L);

        // Then
        assertThat(exists).isFalse();
    }

    @Test
    @DisplayName("save - Should update postEntity successfully")
    void testUpdate() {
        // Given
        PostEntity savedPostEntity = entityManager.persistAndFlush(postEntity);
        String originalTitle = savedPostEntity.getTitle();

        // When
        savedPostEntity.setTitle("Updated Title");
        savedPostEntity.setUpdatedAt(LocalDateTime.now());
        PostEntity updatedPostEntity = postRepository.save(savedPostEntity);
        entityManager.flush();
        entityManager.clear();

        // Then
        PostEntity foundPostEntity = entityManager.find(PostEntity.class, updatedPostEntity.getId());
        assertThat(foundPostEntity).isNotNull();
        assertThat(foundPostEntity.getTitle()).isEqualTo("Updated Title");
        assertThat(foundPostEntity.getTitle()).isNotEqualTo(originalTitle);
    }

    @Test
    @DisplayName("save - Should maintain many-to-many relationship with tagEntities")
    void testManyToMany_WithTags() {
        // Given
        PostEntity savedPostEntity = entityManager.persistAndFlush(postEntity);
        entityManager.flush();
        entityManager.clear();

        // When
        PostEntity foundPostEntity = entityManager.find(PostEntity.class, savedPostEntity.getId());
        TagEntity foundTagEntity1 = entityManager.find(TagEntity.class, tagEntity1.getId());
        TagEntity foundTagEntity2 = entityManager.find(TagEntity.class, tagEntity2.getId());

        // Then
        assertThat(foundPostEntity).isNotNull();
        assertThat(foundPostEntity.getTagEntities()).hasSize(2);
        assertThat(foundTagEntity1.getPostEntities()).contains(foundPostEntity);
        assertThat(foundTagEntity2.getPostEntities()).contains(foundPostEntity);
    }
}