package com.sample.projects.postandcomments.entity;

import com.sample.projects.postandcomments.config.TestJpaAuditingConfig;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.context.annotation.Import;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

@Slf4j
@DisplayName("PostEntity Entity Tests")
@Import(TestJpaAuditingConfig.class)
class PostEntityTest {

    private PostEntity postEntity;
    private PostCommentsEntity comment1;
    private PostCommentsEntity comment2;
    private PostDetailEntity postDetailsEntity;
    private TagEntity tagEntity1;
    private TagEntity tagEntity2;
    private Validator validator;

    @BeforeEach
    void setUp() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
        postEntity = PostEntity.builder()
                .id(1L)
                .title("Test PostEntity")
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .comments(new ArrayList<>())
                .tagEntities(new LinkedHashSet<>())
                .build();

        comment1 = PostCommentsEntity.builder()
                .id(1L)
                .comment("First comment")
                .build();

        comment2 = PostCommentsEntity.builder()
                .id(2L)
                .comment("Second comment")
                .build();

        postDetailsEntity = PostDetailEntity.builder()
                .id(1L)
                .description("Test description")
                .build();

        tagEntity1 = TagEntity.builder()
                .id(1L)
                .name("Spring Boot")
                .postEntities(new LinkedHashSet<>())
                .build();

        tagEntity2 = TagEntity.builder()
                .id(2L)
                .name("Java")
                .postEntities(new LinkedHashSet<>())
                .build();
    }

    @Test
    @DisplayName("addComment - Should add comment and set bidirectional relationship")
    void testAddComment() {
        // When
        postEntity.addComment(comment1);

        // Then
        assertThat(postEntity.getComments()).hasSize(1);
        assertThat(postEntity.getComments()).contains(comment1);
        assertThat(comment1.getPostEntity()).isEqualTo(postEntity);
    }

    @Test
    @DisplayName("addComment - Should add multiple comments")
    void testAddComment_Multiple() {
        // When
        postEntity.addComment(comment1);
        postEntity.addComment(comment2);

        // Then
        assertThat(postEntity.getComments()).hasSize(2);
        assertThat(postEntity.getComments()).contains(comment1, comment2);
        assertThat(comment1.getPostEntity()).isEqualTo(postEntity);
        assertThat(comment2.getPostEntity()).isEqualTo(postEntity);
    }

    @Test
    @DisplayName("removeComment - Should remove comment and clear bidirectional relationship")
    void testRemoveComment() {
        // Given
        postEntity.addComment(comment1);
        postEntity.addComment(comment2);

        // When
        postEntity.removeComment(comment1);

        // Then
        assertThat(postEntity.getComments()).hasSize(1);
        assertThat(postEntity.getComments()).contains(comment2);
        assertThat(postEntity.getComments()).doesNotContain(comment1);
        assertThat(comment1.getPostEntity()).isNull();
        assertThat(comment2.getPostEntity()).isEqualTo(postEntity);
    }

    @Test
    @DisplayName("addTag - Should add tag and synchronize bidirectional relationship")
    void testAddTag() {
        // When
        postEntity.addTag(tagEntity1);

        // Then
        assertThat(postEntity.getTagEntities()).hasSize(1);
        assertThat(postEntity.getTagEntities()).contains(tagEntity1);
        assertThat(tagEntity1.getPostEntities()).contains(postEntity);
    }

    @Test
    @DisplayName("addTag - Should add multiple tagEntities")
    void testAddTag_Multiple() {
        // When
        postEntity.addTag(tagEntity1);
        postEntity.addTag(tagEntity2);

        // Then
        assertThat(postEntity.getTagEntities()).hasSize(2);
        assertThat(postEntity.getTagEntities()).contains(tagEntity1, tagEntity2);
        assertThat(tagEntity1.getPostEntities()).contains(postEntity);
        assertThat(tagEntity2.getPostEntities()).contains(postEntity);
    }

    @Test
    @DisplayName("removeTag - Should remove tag and synchronize bidirectional relationship")
    void testRemoveTag() {
        // Given
        postEntity.addTag(tagEntity1);
        postEntity.addTag(tagEntity2);

        // When
        postEntity.removeTag(tagEntity1);

        // Then
        assertThat(postEntity.getTagEntities()).hasSize(1);
        assertThat(postEntity.getTagEntities()).contains(tagEntity2);
        assertThat(postEntity.getTagEntities()).doesNotContain(tagEntity1);
        assertThat(tagEntity1.getPostEntities()).doesNotContain(postEntity);
        assertThat(tagEntity2.getPostEntities()).contains(postEntity);
    }

    @Test
    @DisplayName("setDetails - Should set PostDetailEntity and synchronize bidirectional relationship")
    void testSetDetails_Add() {
        // When
        postEntity.setDetails(postDetailsEntity);

        // Then
        assertThat(postEntity.getPostDetailEntity()).isEqualTo(postDetailsEntity);
        assertThat(postDetailsEntity.getPostEntity()).isEqualTo(postEntity);
    }

    @Test
    @DisplayName("setDetails - Should replace existing PostDetailEntity")
    void testSetDetails_Replace() {
        // Given
        PostDetailEntity oldDetails = PostDetailEntity.builder()
                .id(2L)
                .description("Old description")
                .build();

        postEntity.setDetails(oldDetails);

        // When
        postEntity.setDetails(postDetailsEntity);

        // Then
        assertThat(postEntity.getPostDetailEntity()).isEqualTo(postDetailsEntity);
        assertThat(postDetailsEntity.getPostEntity()).isEqualTo(postEntity);
        assertThat(oldDetails.getPostEntity()).isNotNull();
    }

    @Test
    @DisplayName("setDetails - Should remove PostDetailEntity when set to null")
    void testSetDetails_Remove() {
        // Given
        postEntity.setDetails(postDetailsEntity);
        assertThat(postEntity.getPostDetailEntity()).isNotNull();

        // When
        postEntity.setDetails(null);

        // Then
        assertThat(postEntity.getPostDetailEntity()).isNull();
        assertThat(postDetailsEntity.getPostEntity()).isNull();
    }

    @Test
    @DisplayName("equals - Should return true for same instance")
    void testEquals_SameInstance() {
        // When/Then
        assertThat(postEntity.equals(postEntity)).isTrue();
    }

    @Test
    @DisplayName("equals - Should return true for postEntities with same id")
    void testEquals_SameId() {
        // Given
        PostEntity postEntity2 = PostEntity.builder()
                .id(1L)
                .title("Different Title")
                .build();

        // When/Then
        assertThat(postEntity.equals(postEntity2)).isTrue();
    }

    @Test
    @DisplayName("equals - Should return false for postEntities with different id")
    void testEquals_DifferentId() {
        // Given
        PostEntity postEntity2 = PostEntity.builder()
                .id(2L)
                .title("Test PostEntity")
                .build();

        // When/Then
        assertThat(postEntity.equals(postEntity2)).isFalse();
    }

    @Test
    @DisplayName("equals - Should return false for null")
    void testEquals_Null() {
        // When/Then
        assertThat(postEntity.equals(null)).isFalse();
    }

    @Test
    @DisplayName("equals - Should return false for different type")
    void testEquals_DifferentType() {
        // When/Then
        assertThat(postEntity.equals("Not a PostEntity")).isFalse();
    }

    @Test
    @DisplayName("equals - Should return false when id is null")
    void testEquals_NullId() {
        // Given
        PostEntity postEntityWithoutId = PostEntity.builder()
                .title("Test PostEntity")
                .build();

        PostEntity postEntity2 = PostEntity.builder()
                .id(1L)
                .title("Test PostEntity")
                .build();

        // When/Then
        assertThat(postEntityWithoutId.equals(postEntity2)).isFalse();
    }

    @Test
    @DisplayName("hashCode - Should return same hashCode for postEntities with same id")
    void testHashCode() {
        // Given
        PostEntity postEntity2 = PostEntity.builder()
                .id(1L)
                .title("Different Title")
                .build();

        // When/Then
        assertThat(postEntity.hashCode()).isEqualTo(postEntity2.hashCode());
    }

    @Test
    @DisplayName("hashCode - Should return class hashCode when id is null")
    void testHashCode_NullId() {
        // Given
        PostEntity postEntityWithoutId = PostEntity.builder()
                .title("Test PostEntity")
                .build();

        // When/Then
        assertThat(postEntityWithoutId.hashCode()).isEqualTo(PostEntity.class.hashCode());
    }

    @Test
    @DisplayName("Builder - Should create PostEntity with all fields")
    void testBuilder() {
        // Given
        LocalDateTime now = LocalDateTime.now();
        Set<TagEntity> tagEntities = new LinkedHashSet<>(Set.of(tagEntity1, tagEntity2));
        ArrayList<PostCommentsEntity> comments = new ArrayList<>(Set.of(comment1, comment2));

        // When
        PostEntity builtPostEntity = PostEntity.builder()
                .id(1L)
                .title("Test PostEntity")
                .postDetailEntity(postDetailsEntity)
                .comments(comments)
                .tagEntities(tagEntities)
                .createdAt(now)
                .updatedAt(now)
                .build();

        // Then
        assertThat(builtPostEntity.getId()).isEqualTo(1L);
        assertThat(builtPostEntity.getTitle()).isEqualTo("Test PostEntity");
        assertThat(builtPostEntity.getPostDetailEntity()).isEqualTo(postDetailsEntity);
        assertThat(builtPostEntity.getComments()).isEqualTo(comments);
        assertThat(builtPostEntity.getTagEntities()).isEqualTo(tagEntities);
        assertThat(builtPostEntity.getCreatedAt()).isEqualTo(now);
        assertThat(builtPostEntity.getUpdatedAt()).isEqualTo(now);
    }

    @Test
    @DisplayName("NoArgsConstructor - Should create empty PostEntity")
    void testNoArgsConstructor() {
        // When
        PostEntity emptyPostEntity = new PostEntity();

        // Then
        assertThat(emptyPostEntity).isNotNull();
        assertThat(emptyPostEntity.getId()).isNull();
        assertThat(emptyPostEntity.getTitle()).isNull();
    }

    @Test
    @DisplayName("Validation - Should pass when title is valid")
    void testValidation_ValidTitle() {
        // Given
        PostEntity validPostEntity = PostEntity.builder()
                .title("Valid PostEntity Title")
                .build();

        // When
        Set<ConstraintViolation<PostEntity>> violations = validator.validate(validPostEntity);

        // Then
        assertThat(violations).isEmpty();
    }

    @Test
    @DisplayName("Validation - Should fail when title is null")
    void testValidation_NullTitle() {
        // Given
        PostEntity postEntityWithNullTitle = PostEntity.builder()
                .title(null)
                .build();

        // When
        Set<ConstraintViolation<PostEntity>> violations = validator.validate(postEntityWithNullTitle);

        // Then
        assertThat(violations).hasSize(1);
        assertThat(violations.iterator().next().getMessage()).isEqualTo("Title is required");
        assertThat(violations.iterator().next().getPropertyPath().toString()).isEqualTo("title");
    }

    @Test
    @DisplayName("Validation - Should fail when title is blank")
    void testValidation_BlankTitle() {
        // Given
        PostEntity postEntityWithBlankTitle = PostEntity.builder()
                .title("")
                .build();

        // When
        Set<ConstraintViolation<PostEntity>> violations = validator.validate(postEntityWithBlankTitle);

        // Then
        // Empty string violates both @NotBlank and @Size(min = 1) constraints
        assertThat(violations).hasSize(2);
        assertThat(violations).extracting("message")
                .contains("Title is required", "Title must be between 1 and 255 characters");
    }

    @Test
    @DisplayName("Validation - Should fail when title exceeds 255 characters")
    void testValidation_TitleExceedsMaxLength() {
        // Given
        String longTitle = "A".repeat(256);
        PostEntity postEntityWithLongTitle = PostEntity.builder()
                .title(longTitle)
                .build();

        // When
        Set<ConstraintViolation<PostEntity>> violations = validator.validate(postEntityWithLongTitle);

        // Then
        assertThat(violations).hasSize(1);
        assertThat(violations.iterator().next().getMessage()).contains("Title must be between 1 and 255 characters");
    }

    @Test
    @DisplayName("Validation - Should pass when title is exactly 255 characters")
    void testValidation_TitleAtMaxLength() {
        // Given
        String maxLengthTitle = "A".repeat(255);
        PostEntity postEntityWithMaxLengthTitle = PostEntity.builder()
                .title(maxLengthTitle)
                .build();

        // When
        Set<ConstraintViolation<PostEntity>> violations = validator.validate(postEntityWithMaxLengthTitle);

        // Then
        assertThat(violations).isEmpty();
    }
}