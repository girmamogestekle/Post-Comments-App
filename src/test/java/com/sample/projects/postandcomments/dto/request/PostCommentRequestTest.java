package com.sample.projects.postandcomments.dto.request;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@Slf4j
@DisplayName("PostCommentRequest DTO/Validation Tests")
public class PostCommentRequestTest {

    private Validator validator;

    @BeforeEach
    void setUp() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    @DisplayName("Valid PostComment - Should Pass Validation")
    void testValidPostCommentRequest(){
        // Given
        PostCommentRequest postCommentRequest = PostCommentRequest.builder()
                .postId(1L)
                .comment("Valid PostComment Review")
                .build();

        // When
        Set<ConstraintViolation<PostCommentRequest>> violations = validator.validate(postCommentRequest);

        // Then
        assertThat(violations.isEmpty());
        assertThat(violations.size()).isEqualTo(0);
    }

    @Test
    @DisplayName("Invalid PostCommentRequest - Should Fail When Review Is Null")
    void testInvalidPostCommentRequest_NullReview(){
        // Given
        PostCommentRequest postCommentRequest = PostCommentRequest.builder()
                .comment(null)
                .postId(1L)
                .build();

        // When
        Set<ConstraintViolation<PostCommentRequest>> violations = validator.validate(postCommentRequest);

        // Then
        assertThat(violations.size()).isEqualTo(1);
        assertThat(violations.iterator().next().getMessage()).isEqualTo("Review Is Required");
        assertThat(violations.iterator().next().getPropertyPath().toString()).isEqualTo("comment");
    }

    @Test
    @DisplayName("Invalid PostCommentRequest - Should Fail When Review Is Blank")
    void testInvalidPostCommentRequest_BlankReview(){
        // Given
        PostCommentRequest postCommentRequest = PostCommentRequest.builder()
                .comment("")
                .postId(1L)
                .build();

        // When
        Set<ConstraintViolation<PostCommentRequest>> violations = validator.validate(postCommentRequest);

        // Then
        // Empty String Violates @NotBlank Constraints
        assertThat(violations.size()).isEqualTo(1);
        assertThat(violations.iterator().next().getMessage()).isEqualTo("Review Is Required");
    }

    @Test
    @DisplayName("Invalid PostCommentRequest - Should Fail When Review Is Whitespace Only")
    void testInvalidPostCommentRequest_WhitespaceReview(){
        // Given
        PostCommentRequest postCommentRequest = PostCommentRequest.builder()
                .comment(" ")
                .postId(1L)
                .build();

        // When
        Set<ConstraintViolation<PostCommentRequest>> violations = validator.validate(postCommentRequest);

        // Then
        assertThat(violations.size()).isEqualTo(1);
        assertThat(violations.iterator().next().getMessage()).isEqualTo("Review Is Required");
    }

    @Test
    @DisplayName("Invalid PostCommentRequest - Should Fail When PostId Is Null")
    void testInvalidPostCommentRequest_NullPostId(){
        // Given
        PostCommentRequest postCommentRequest = PostCommentRequest.builder()
                .comment("Post Comment Test")
                .postId(null)
                .build();

        // When
        Set<ConstraintViolation<PostCommentRequest>> violations = validator.validate(postCommentRequest);

        // Then
        assertThat(violations.size()).isEqualTo(1);
        assertThat(violations.iterator().next().getMessage()).isEqualTo("PostEntity ID is required");
    }

    @Test
    @DisplayName("Builder - Should Create PostCommentRequest With All Fields")
    void testPostCommentRequest_WithAllFields(){
        PostCommentRequest postCommentRequest = PostCommentRequest.builder()
                .comment("Test Post Comment")
                .postId(1L)
                .build();

        // Then
        assertThat(postCommentRequest.getComment()).isEqualTo("Test Post Comment");
        assertThat(postCommentRequest.getPostId()).isEqualTo(1L);
    }

    @Test
    @DisplayName("NoArgsConstructor - Should Create Empty PostCommentRequest")
    void testNoArgsConstructor_EmptyPostCommentRequest(){
        // When
        PostCommentRequest postCommentRequest = new PostCommentRequest();

        // Then
        assertThat(postCommentRequest).isNotNull();
        assertThat(postCommentRequest.getPostId()).isNull();
        assertThat(postCommentRequest.getComment()).isNull();
    }

    @Test
    @DisplayName("AllArgsConstructor - Should Create PostCommentRequest Will All Parameters")
    void testAllArgsConstructor_PostCommentRequest(){
        // Given
        PostCommentRequest postCommentRequest = new PostCommentRequest("Test Post Comment", 1L);

        // Then
        assertThat(postCommentRequest.getComment()).isEqualTo("Test Post Comment");
        assertThat(postCommentRequest.getPostId()).isEqualTo(1L);
    }

    @Test
    @DisplayName("Getters And Setters - Should Work Correctly")
    void testGettersAndSetters_PostCommentRequest(){
        // Given
        PostCommentRequest postCommentRequest = new PostCommentRequest();

        // When
        postCommentRequest.setComment("Test Post Comment");
        postCommentRequest.setPostId(1L);

        // Then
        assertThat(postCommentRequest.getComment()).isEqualTo("Test Post Comment");
        assertThat(postCommentRequest.getPostId()).isEqualTo(1L);
    }

}
