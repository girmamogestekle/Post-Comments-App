package com.sample.projects.postandcomments.dto.request;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("PostRequest DTO/Validation Tests")
class PostRequestTest {

    private Validator validator;

    @BeforeEach
    void setUp() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    @DisplayName("Valid PostRequest - Should pass validation")
    void testValidPostRequest() {
        // Given
        PostRequest request = PostRequest.builder()
                .title("Valid Post Title")
                .tagIds(Set.of(1L, 2L))
                .build();

        // When
        Set<ConstraintViolation<PostRequest>> violations = validator.validate(request);

        // Then
        assertThat(violations).isEmpty();
    }

    @Test
    @DisplayName("Valid PostRequest without tags - Should pass validation")
    void testValidPostRequest_WithoutTags() {
        // Given
        PostRequest request = PostRequest.builder()
                .title("Valid Post Title")
                .build();

        // When
        Set<ConstraintViolation<PostRequest>> violations = validator.validate(request);

        // Then
        assertThat(violations).isEmpty();
    }

    @Test
    @DisplayName("Invalid PostRequest - Should fail when title is null")
    void testInvalidPostRequest_NullTitle() {
        // Given
        PostRequest request = PostRequest.builder()
                .title(null)
                .build();

        // When
        Set<ConstraintViolation<PostRequest>> violations = validator.validate(request);

        // Then
        assertThat(violations).hasSize(1);
        assertThat(violations.iterator().next().getMessage()).isEqualTo("Title is required");
        assertThat(violations.iterator().next().getPropertyPath().toString()).isEqualTo("title");
    }

    @Test
    @DisplayName("Invalid PostRequest - Should fail when title is blank")
    void testInvalidPostRequest_BlankTitle() {
        // Given
        PostRequest request = PostRequest.builder()
                .title("")
                .build();

        // When
        Set<ConstraintViolation<PostRequest>> violations = validator.validate(request);

        // Then
        assertThat(violations).hasSize(1);
        assertThat(violations.iterator().next().getMessage()).isEqualTo("Title is required");
    }

    @Test
    @DisplayName("Invalid PostRequest - Should fail when title is whitespace only")
    void testInvalidPostRequest_WhitespaceTitle() {
        // Given
        PostRequest request = PostRequest.builder()
                .title("   ")
                .build();

        // When
        Set<ConstraintViolation<PostRequest>> violations = validator.validate(request);

        // Then
        assertThat(violations).hasSize(1);
        assertThat(violations.iterator().next().getMessage()).isEqualTo("Title is required");
    }

    @Test
    @DisplayName("Builder - Should create PostRequest with all fields")
    void testBuilder() {
        // Given
        Set<Long> tagIds = Set.of(1L, 2L, 3L);

        // When
        PostRequest request = PostRequest.builder()
                .title("Test Post")
                .tagIds(tagIds)
                .build();

        // Then
        assertThat(request.getTitle()).isEqualTo("Test Post");
        assertThat(request.getTagIds()).isEqualTo(tagIds);
        assertThat(request.getTagIds()).hasSize(3);
    }

    @Test
    @DisplayName("Builder - Should create PostRequest with empty tagIds")
    void testBuilder_EmptyTagIds() {
        // When
        PostRequest request = PostRequest.builder()
                .title("Test Post")
                .tagIds(Set.of())
                .build();

        // Then
        assertThat(request.getTitle()).isEqualTo("Test Post");
        assertThat(request.getTagIds()).isEmpty();
    }

    @Test
    @DisplayName("NoArgsConstructor - Should create empty PostRequest")
    void testNoArgsConstructor() {
        // When
        PostRequest request = new PostRequest();

        // Then
        assertThat(request).isNotNull();
        assertThat(request.getTitle()).isNull();
        assertThat(request.getTagIds()).isNull();
    }

    @Test
    @DisplayName("AllArgsConstructor - Should create PostRequest with all parameters")
    void testAllArgsConstructor() {
        // Given
        Set<Long> tagIds = Set.of(1L, 2L);

        // When
        PostRequest request = new PostRequest("Test Post", tagIds);

        // Then
        assertThat(request.getTitle()).isEqualTo("Test Post");
        assertThat(request.getTagIds()).isEqualTo(tagIds);
    }

    @Test
    @DisplayName("Getters and Setters - Should work correctly")
    void testGettersAndSetters() {
        // Given
        PostRequest request = new PostRequest();
        Set<Long> tagIds = Set.of(1L, 2L);

        // When
        request.setTitle("Test Post");
        request.setTagIds(tagIds);

        // Then
        assertThat(request.getTitle()).isEqualTo("Test Post");
        assertThat(request.getTagIds()).isEqualTo(tagIds);
    }

    @Test
    @DisplayName("Valid PostRequest - Should accept long title")
    void testValidPostRequest_LongTitle() {
        // Given
        String longTitle = "A".repeat(1000);
        PostRequest request = PostRequest.builder()
                .title(longTitle)
                .build();

        // When
        Set<ConstraintViolation<PostRequest>> violations = validator.validate(request);

        // Then
        assertThat(violations).isEmpty();
    }

    @Test
    @DisplayName("Valid PostRequest - Should accept special characters in title")
    void testValidPostRequest_SpecialCharacters() {
        // Given
        PostRequest request = PostRequest.builder()
                .title("Post with Special Characters: !@#$%^&*()")
                .build();

        // When
        Set<ConstraintViolation<PostRequest>> violations = validator.validate(request);

        // Then
        assertThat(violations).isEmpty();
    }
}

