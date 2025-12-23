package com.sample.projects.postandcomments.entity;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

@Slf4j
@DisplayName("Post Entity Tests")
class PostEntityTest {

    private Post post;
    private PostComment comment1;
    private PostComment comment2;
    private PostDetails postDetails;
    private Tag tag1;
    private Tag tag2;
    private Validator validator;

    @BeforeEach
    void setUp() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
        post = Post.builder()
                .id(1L)
                .title("Test Post")
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .comments(new ArrayList<>())
                .tags(new LinkedHashSet<>())
                .build();

        comment1 = PostComment.builder()
                .id(1L)
                .review("First comment")
                .build();

        comment2 = PostComment.builder()
                .id(2L)
                .review("Second comment")
                .build();

        postDetails = PostDetails.builder()
                .id(1L)
                .description("Test description")
                .build();

        tag1 = Tag.builder()
                .id(1L)
                .name("Spring Boot")
                .posts(new LinkedHashSet<>())
                .build();

        tag2 = Tag.builder()
                .id(2L)
                .name("Java")
                .posts(new LinkedHashSet<>())
                .build();
    }

    @Test
    @DisplayName("addComment - Should add comment and set bidirectional relationship")
    void testAddComment() {
        // When
        post.addComment(comment1);

        // Then
        assertThat(post.getComments()).hasSize(1);
        assertThat(post.getComments()).contains(comment1);
        assertThat(comment1.getPost()).isEqualTo(post);
    }

    @Test
    @DisplayName("addComment - Should add multiple comments")
    void testAddComment_Multiple() {
        // When
        post.addComment(comment1);
        post.addComment(comment2);

        // Then
        assertThat(post.getComments()).hasSize(2);
        assertThat(post.getComments()).contains(comment1, comment2);
        assertThat(comment1.getPost()).isEqualTo(post);
        assertThat(comment2.getPost()).isEqualTo(post);
    }

    @Test
    @DisplayName("removeComment - Should remove comment and clear bidirectional relationship")
    void testRemoveComment() {
        // Given
        post.addComment(comment1);
        post.addComment(comment2);

        // When
        post.removeComment(comment1);

        // Then
        assertThat(post.getComments()).hasSize(1);
        assertThat(post.getComments()).contains(comment2);
        assertThat(post.getComments()).doesNotContain(comment1);
        assertThat(comment1.getPost()).isNull();
        assertThat(comment2.getPost()).isEqualTo(post);
    }

    @Test
    @DisplayName("addTag - Should add tag and synchronize bidirectional relationship")
    void testAddTag() {
        // When
        post.addTag(tag1);

        // Then
        assertThat(post.getTags()).hasSize(1);
        assertThat(post.getTags()).contains(tag1);
        assertThat(tag1.getPosts()).contains(post);
    }

    @Test
    @DisplayName("addTag - Should add multiple tags")
    void testAddTag_Multiple() {
        // When
        post.addTag(tag1);
        post.addTag(tag2);

        // Then
        assertThat(post.getTags()).hasSize(2);
        assertThat(post.getTags()).contains(tag1, tag2);
        assertThat(tag1.getPosts()).contains(post);
        assertThat(tag2.getPosts()).contains(post);
    }

    @Test
    @DisplayName("removeTag - Should remove tag and synchronize bidirectional relationship")
    void testRemoveTag() {
        // Given
        post.addTag(tag1);
        post.addTag(tag2);

        // When
        post.removeTag(tag1);

        // Then
        assertThat(post.getTags()).hasSize(1);
        assertThat(post.getTags()).contains(tag2);
        assertThat(post.getTags()).doesNotContain(tag1);
        assertThat(tag1.getPosts()).doesNotContain(post);
        assertThat(tag2.getPosts()).contains(post);
    }

    @Test
    @DisplayName("setDetails - Should set PostDetails and synchronize bidirectional relationship")
    void testSetDetails_Add() {
        // When
        post.setDetails(postDetails);

        // Then
        assertThat(post.getPostDetails()).isEqualTo(postDetails);
        assertThat(postDetails.getPost()).isEqualTo(post);
    }

    @Test
    @DisplayName("setDetails - Should replace existing PostDetails")
    void testSetDetails_Replace() {
        // Given
        PostDetails oldDetails = PostDetails.builder()
                .id(2L)
                .description("Old description")
                .build();

        post.setDetails(oldDetails);

        // When
        post.setDetails(postDetails);

        // Then
        assertThat(post.getPostDetails()).isEqualTo(postDetails);
        assertThat(postDetails.getPost()).isEqualTo(post);
        assertThat(oldDetails.getPost()).isNotNull();
    }

    @Test
    @DisplayName("setDetails - Should remove PostDetails when set to null")
    void testSetDetails_Remove() {
        // Given
        post.setDetails(postDetails);
        assertThat(post.getPostDetails()).isNotNull();

        // When
        post.setDetails(null);

        // Then
        assertThat(post.getPostDetails()).isNull();
        assertThat(postDetails.getPost()).isNull();
    }

    @Test
    @DisplayName("equals - Should return true for same instance")
    void testEquals_SameInstance() {
        // When/Then
        assertThat(post.equals(post)).isTrue();
    }

    @Test
    @DisplayName("equals - Should return true for posts with same id")
    void testEquals_SameId() {
        // Given
        Post post2 = Post.builder()
                .id(1L)
                .title("Different Title")
                .build();

        // When/Then
        assertThat(post.equals(post2)).isTrue();
    }

    @Test
    @DisplayName("equals - Should return false for posts with different id")
    void testEquals_DifferentId() {
        // Given
        Post post2 = Post.builder()
                .id(2L)
                .title("Test Post")
                .build();

        // When/Then
        assertThat(post.equals(post2)).isFalse();
    }

    @Test
    @DisplayName("equals - Should return false for null")
    void testEquals_Null() {
        // When/Then
        assertThat(post.equals(null)).isFalse();
    }

    @Test
    @DisplayName("equals - Should return false for different type")
    void testEquals_DifferentType() {
        // When/Then
        assertThat(post.equals("Not a Post")).isFalse();
    }

    @Test
    @DisplayName("equals - Should return false when id is null")
    void testEquals_NullId() {
        // Given
        Post postWithoutId = Post.builder()
                .title("Test Post")
                .build();

        Post post2 = Post.builder()
                .id(1L)
                .title("Test Post")
                .build();

        // When/Then
        assertThat(postWithoutId.equals(post2)).isFalse();
    }

    @Test
    @DisplayName("hashCode - Should return same hashCode for posts with same id")
    void testHashCode() {
        // Given
        Post post2 = Post.builder()
                .id(1L)
                .title("Different Title")
                .build();

        // When/Then
        assertThat(post.hashCode()).isEqualTo(post2.hashCode());
    }

    @Test
    @DisplayName("hashCode - Should return class hashCode when id is null")
    void testHashCode_NullId() {
        // Given
        Post postWithoutId = Post.builder()
                .title("Test Post")
                .build();

        // When/Then
        assertThat(postWithoutId.hashCode()).isEqualTo(Post.class.hashCode());
    }

    @Test
    @DisplayName("Builder - Should create Post with all fields")
    void testBuilder() {
        // Given
        LocalDateTime now = LocalDateTime.now();
        Set<Tag> tags = new LinkedHashSet<>(Set.of(tag1, tag2));
        ArrayList<PostComment> comments = new ArrayList<>(Set.of(comment1, comment2));

        // When
        Post builtPost = Post.builder()
                .id(1L)
                .title("Test Post")
                .postDetails(postDetails)
                .comments(comments)
                .tags(tags)
                .createdAt(now)
                .updatedAt(now)
                .build();

        // Then
        assertThat(builtPost.getId()).isEqualTo(1L);
        assertThat(builtPost.getTitle()).isEqualTo("Test Post");
        assertThat(builtPost.getPostDetails()).isEqualTo(postDetails);
        assertThat(builtPost.getComments()).isEqualTo(comments);
        assertThat(builtPost.getTags()).isEqualTo(tags);
        assertThat(builtPost.getCreatedAt()).isEqualTo(now);
        assertThat(builtPost.getUpdatedAt()).isEqualTo(now);
    }

    @Test
    @DisplayName("NoArgsConstructor - Should create empty Post")
    void testNoArgsConstructor() {
        // When
        Post emptyPost = new Post();

        // Then
        assertThat(emptyPost).isNotNull();
        assertThat(emptyPost.getId()).isNull();
        assertThat(emptyPost.getTitle()).isNull();
    }

    @Test
    @DisplayName("Validation - Should pass when title is valid")
    void testValidation_ValidTitle() {
        // Given
        Post validPost = Post.builder()
                .title("Valid Post Title")
                .build();

        // When
        Set<ConstraintViolation<Post>> violations = validator.validate(validPost);

        // Then
        assertThat(violations).isEmpty();
    }

    @Test
    @DisplayName("Validation - Should fail when title is null")
    void testValidation_NullTitle() {
        // Given
        Post postWithNullTitle = Post.builder()
                .title(null)
                .build();

        // When
        Set<ConstraintViolation<Post>> violations = validator.validate(postWithNullTitle);

        // Then
        assertThat(violations).hasSize(1);
        assertThat(violations.iterator().next().getMessage()).isEqualTo("Title is required");
        assertThat(violations.iterator().next().getPropertyPath().toString()).isEqualTo("title");
    }

    @Test
    @DisplayName("Validation - Should fail when title is blank")
    void testValidation_BlankTitle() {
        // Given
        Post postWithBlankTitle = Post.builder()
                .title("")
                .build();

        // When
        Set<ConstraintViolation<Post>> violations = validator.validate(postWithBlankTitle);

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
        Post postWithLongTitle = Post.builder()
                .title(longTitle)
                .build();

        // When
        Set<ConstraintViolation<Post>> violations = validator.validate(postWithLongTitle);

        // Then
        assertThat(violations).hasSize(1);
        assertThat(violations.iterator().next().getMessage()).contains("Title must be between 1 and 255 characters");
    }

    @Test
    @DisplayName("Validation - Should pass when title is exactly 255 characters")
    void testValidation_TitleAtMaxLength() {
        // Given
        String maxLengthTitle = "A".repeat(255);
        Post postWithMaxLengthTitle = Post.builder()
                .title(maxLengthTitle)
                .build();

        // When
        Set<ConstraintViolation<Post>> violations = validator.validate(postWithMaxLengthTitle);

        // Then
        assertThat(violations).isEmpty();
    }
}