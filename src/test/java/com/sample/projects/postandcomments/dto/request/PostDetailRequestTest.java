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

import static org.assertj.core.api.Assertions.assertThat;

@Slf4j
@DisplayName("Post Detail Request DTO Test")
public class PostDetailRequestTest {

    private Validator validator;

    @BeforeEach
    void setUp(){
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    @DisplayName("Valid Post Detail - Should Pass Validation")
    void testValidPostDetailRequest(){
        // Given
        PostDetailRequest postDetailRequest = PostDetailRequest.builder()
                .postId(1L)
                .description("Valid Post Detail Description")
                .build();

        // When
        Set<ConstraintViolation<PostDetailRequest>> violations = validator.validate(postDetailRequest);

        // Then
        assertThat(violations.isEmpty());
        assertThat(violations.size()).isEqualTo(0);
    }

    @Test
    @DisplayName("🚫 Invalid Post Detail Request - Should Fail When Description Is Null")
    void testInvalidPostDetailRequest_NullDescription(){
        // Given
        PostDetailRequest postDetailRequest = PostDetailRequest.builder()
                .postId(1L)
                .description(null)
                .build();

        // When
        Set<ConstraintViolation<PostDetailRequest>> violations = validator.validate(postDetailRequest);

        // Then
        assertThat(violations.size()).isEqualTo(2);
        assertThat(violations.iterator().next().getMessage()).isEqualTo("Description Is Required");
        assertThat(violations.iterator().next().getPropertyPath().toString()).isEqualTo("description");
    }

    @Test
    @DisplayName("🚫 Invalid Post Detail Request - Should Fail When Description Is Blank")
    void testInvalidPostDetailRequest_BlankDescription(){
        // Given
        PostDetailRequest postDetailRequest = PostDetailRequest.builder()
                .postId(1L)
                .description("")
                .build();

        // When
        Set<ConstraintViolation<PostDetailRequest>> violations = validator.validate(postDetailRequest);

        // Then
        assertThat(violations.size()).isEqualTo(1);
        assertThat(violations.iterator().next().getMessage()).isEqualTo("Description Does Not Blank");
        assertThat(violations.iterator().next().getPropertyPath().toString()).isEqualTo("description");
    }

    @Test
    @DisplayName("🚫 Invalid Post Detail Request - Should Fail When Description Is Whitespace")
    void testInvalidPostDetailRequest_WhitespaceDescription(){
        // Given
        PostDetailRequest postDetailRequest = PostDetailRequest.builder()
                .postId(1L)
                .description(" ")
                .build();

        // When
        Set<ConstraintViolation<PostDetailRequest>> violations = validator.validate(postDetailRequest);

        // Then
        assertThat(violations.size()).isEqualTo(1);
        assertThat(violations.iterator().next().getMessage()).isEqualTo("Description Does Not Blank");
        assertThat(violations.iterator().next().getPropertyPath().toString()).isEqualTo("description");
    }

    @Test
    @DisplayName("🚫 Invalid Post Detail Request - Should Fail When Post Id Is Null")
    void testInvalidPostDetailRequest_NullPostId(){
        // Given
        PostDetailRequest postDetailRequest = PostDetailRequest.builder()
                .postId(null)
                .description("Valid Post Detail Description")
                .build();

        // When
        Set<ConstraintViolation<PostDetailRequest>> violations = validator.validate(postDetailRequest);

        // Then
        assertThat(violations.size()).isEqualTo(1);
        assertThat(violations.iterator().next().getMessage()).isEqualTo("Post Id Is Required");
    }

    @Test
    @DisplayName("Post Detail Request Builder - Should Create Post Detail Request With All Fields")
    void testPostDetailRequest_WithAllFields(){
        // Given
        PostDetailRequest postDetailRequest = PostDetailRequest.builder()
                .postId(1L)
                .description("Valid Post Detail Description")
                .build();

        // Then
        assertThat(postDetailRequest.getPostId()).isEqualTo(1L);
        assertThat(postDetailRequest.getDescription()).isEqualTo("Valid Post Detail Description");
    }

    @Test
    @DisplayName("Post Detail Request No Args Constructor - Should Create Empty Post Detail Request")
    void testPostDetailRequest_WithNoArgsConstructor(){
        // Given
        PostDetailRequest postDetailRequest = new PostDetailRequest();

        // Then
        assertThat(postDetailRequest).isNotNull();
        assertThat(postDetailRequest.getPostId()).isNull();
        assertThat(postDetailRequest.getDescription()).isNull();
    }

    @Test
    @DisplayName("Post Detail Request All Args Constructor - Should Create Post Detail Request With All Parameters")
    void testPostDetailRequest_WithAllArgsConstructor(){
        // Given
        PostDetailRequest postDetailRequest = new PostDetailRequest(1L , "Valid Post Detail Description");

        // Then
        assertThat(postDetailRequest.getPostId()).isEqualTo(1L);
        assertThat(postDetailRequest.getDescription()).isEqualTo("Valid Post Detail Description");
    }

    @Test
    @DisplayName("Post Detail Request Setters And Getters - Should Work Correctly")
    void testPostDetailRequest_WithGettersAndSetters(){
        // Given
        PostDetailRequest postDetailRequest = new PostDetailRequest();

        // When
        postDetailRequest.setPostId(1L);
        postDetailRequest.setDescription("Valid Post Detail Description");

        // Then
        assertThat(postDetailRequest.getPostId()).isEqualTo(1L);
        assertThat(postDetailRequest.getDescription()).isEqualTo("Valid Post Detail Description");
    }

}
