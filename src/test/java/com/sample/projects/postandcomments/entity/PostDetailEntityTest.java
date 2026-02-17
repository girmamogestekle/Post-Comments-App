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
@DisplayName("Post Detail Entity Tests")
@Import(TestJpaAuditingConfig.class)
public class PostDetailEntityTest {

    private PostDetailEntity postDetailEntity;
    private PostEntity postEntity;
    private Validator validator;

    @BeforeEach
    void setUp() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
        postDetailEntity = PostDetailEntity.builder()
                .id(1L)
                .description("Test Description")
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();

        postEntity = PostEntity.builder()
                .id(1L)
                .title("Test Title")
                .postDetailEntity(postDetailEntity)
                .comments(new ArrayList<>())
                .tagEntities(new LinkedHashSet<>())
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();
    }

    @Test
    @DisplayName("Set Post Entity - Should set post detail entity and synchronize bidirectional relationship")
    void testSetPostEntity_Add(){
        // When
        postDetailEntity.setPostEntity(postEntity);

        // Then
        assertThat(postDetailEntity.getPostEntity()).isEqualTo(postEntity);
        assertThat(postEntity.getPostDetailEntity()).isEqualTo(postDetailEntity);
    }

    @Test
    @DisplayName("Set Post Entity - Should update existing Post Entity")
    void testSetPostEntity_Replace(){
        // Given
        PostEntity updatePostEntity = PostEntity.builder()
                .id(1L)
                .title("Test Update Title")
                .postDetailEntity(postDetailEntity)
                .comments(new ArrayList<>())
                .tagEntities(new LinkedHashSet<>())
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();

        // When
        postDetailEntity.setPostEntity(updatePostEntity);

        // Then
        assertThat(postDetailEntity.getPostEntity()).isEqualTo(updatePostEntity);
        assertThat(updatePostEntity.getPostDetailEntity()).isEqualTo(postDetailEntity);
    }

    @Test
    @DisplayName("Set Post Entity - Should remove Post Entity when set to null")
    void testSetPostEntity_Remove(){
        // Given
        postDetailEntity.setPostEntity(postEntity);
        assertThat(postDetailEntity.getPostEntity()).isNotNull();

        // When
        postDetailEntity.setPostEntity(null);

        // Then
        assertThat(postDetailEntity.getPostEntity()).isNull();
    }

    @Test
    @DisplayName("Equals - Should return true for same instance")
    void testEquals_SameInstance(){
        // When / Then
        assertThat(postDetailEntity.equals(postDetailEntity)).isTrue();
    }

    @Test
    @DisplayName("Equals - Should return true for Post Entities with same Id")
    void testEquals_SameId(){
        // Given
        PostDetailEntity postDetailEntity1 = PostDetailEntity.builder()
                .id(1L)
                .postEntity(postEntity)
                .description("Test Description")
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();

        // When / Then
        assertThat(postDetailEntity.equals(postDetailEntity1)).isTrue();
    }

    @Test
    @DisplayName("Equals - Should return false for post detail entity with different Id")
    void testEquals_DifferentId(){
        // Given
        PostDetailEntity postDetailEntity1 = PostDetailEntity.builder()
                .id(2L)
                .postEntity(postEntity)
                .description("Test Description")
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();

        // When / Then
        assertThat(postDetailEntity.equals(postDetailEntity1)).isFalse();
    }

    @Test
    @DisplayName("Equals - Should return false for null")
    void testEquals_Null(){
        // When / Then
        assertThat(postDetailEntity.equals(null)).isFalse();
    }

    @Test
    @DisplayName("Equals - Should return false for different type")
    void testEquals_DifferentType(){
        // When / Then
        assertThat(postDetailEntity.equals("Not a Post Detail Entity Type")).isFalse();
    }

    @Test
    @DisplayName("Equals - Should return false when id is null")
    void testEquals_NullId(){
        // Given
        PostDetailEntity postDetailEntityWithoutId = PostDetailEntity.builder()
                .description("Test Description")
                .postEntity(postEntity)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();

        // Then
        assertThat(postDetailEntity.equals(postDetailEntityWithoutId)).isFalse();
    }

    @Test
    @DisplayName("HashCode - Should return same hashcode for post detail entities with same id")
    void testHashCode(){
        // Given
        PostDetailEntity postDetailEntity2 = PostDetailEntity.builder()
                .id(1L)
                .description("Test Description")
                .postEntity(postEntity)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();

        // Then
        assertThat(postDetailEntity.hashCode()).isEqualTo(postDetailEntity2.hashCode());
    }

    @Test
    @DisplayName("HashCode - Should return class hashcode when id is null")
    void testHashCode_NullId(){
        // Given
        PostDetailEntity postDetailEntityWithoutId = PostDetailEntity.builder()
                .description("Test Description")
                .postEntity(postEntity)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();

        // When / Then
        assertThat(postDetailEntityWithoutId.hashCode()).isEqualTo(PostDetailEntity.class.hashCode());
    }

    @Test
    @DisplayName("Builder - Should create post detail entity will all fields")
    void testBuilder(){
        // Given
        LocalDateTime now = LocalDateTime.now();

        // When
        PostDetailEntity builtPostDetailEntity = PostDetailEntity.builder()
                .id(1L)
                .postEntity(postEntity)
                .description("Test Description")
                .createdAt(now)
                .updatedAt(now)
                .build();

        // Then
        assertThat(builtPostDetailEntity.getId()).isEqualTo(1L);
        assertThat(builtPostDetailEntity.getPostEntity()).isEqualTo(postEntity);
        assertThat(builtPostDetailEntity.getDescription()).isEqualTo("Test Description");
        assertThat(builtPostDetailEntity.getCreatedAt()).isEqualTo(now);
        assertThat(builtPostDetailEntity.getUpdatedAt()).isEqualTo(now);
    }

    @Test
    @DisplayName("NoArgsConstructor - Should create empty post detail entity")
    void testNoArgsConstructor(){
        // When
        PostDetailEntity emptyPostDetailEntity = new PostDetailEntity();

        // Then
        assertThat(emptyPostDetailEntity).isNotNull();
        assertThat(emptyPostDetailEntity.getId()).isNull();
        assertThat(emptyPostDetailEntity.getPostEntity()).isNull();
        assertThat(emptyPostDetailEntity.getCreatedAt()).isNull();
        assertThat(emptyPostDetailEntity.getUpdatedAt()).isNull();
    }

    @Test
    @DisplayName("Description Validation - Should pass when description is valid")
    void testValidation_ValidDescription(){
        // When
        Set<ConstraintViolation<PostDetailEntity>> violations = validator.validate(postDetailEntity);

        // Then
        assertThat(violations).isEmpty();
    }

    @Test
    @DisplayName("Description Validation - Should fail when description is null")
    void testValidation_NullDescription(){
        // Given
        PostDetailEntity postDetailEntityWithNullDescription = PostDetailEntity.builder()
                .id(1L)
                .postEntity(postEntity)
                .description(null)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();

        // When
        Set<ConstraintViolation<PostDetailEntity>> violations = validator.validate(postDetailEntityWithNullDescription);

        // Then
        assertThat(violations).hasSize(1);
        assertThat(violations.iterator().next().getMessage()).isEqualTo("Description is required");
        assertThat(violations.iterator().next().getPropertyPath().toString()).isEqualTo("description");
    }

    @Test
    @DisplayName("Description Validation - Should fail when description is blank")
    void testValidation_BlankDescription(){
        // Given
        PostDetailEntity postDetailEntityWithBlankDescription = PostDetailEntity.builder()
                .id(1L)
                .postEntity(postEntity)
                .description("")
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();

        // When
        Set<ConstraintViolation<PostDetailEntity>> violations = validator.validate(postDetailEntityWithBlankDescription);

        // Then
        // Empty string violates both @NotBlank and @Size(min = 1) constraints
        assertThat(violations).hasSize(2);
        assertThat(violations).extracting("message")
                .contains("Description is required", "Description must be between 1 and 5000 characters");
    }

    @Test
    @DisplayName("Description Validation - Should fail when description exceeds 5000 characters")
    void testValidation_DescriptionExceedsMaxLength(){
        // Given
        String longDescription = "A".repeat(5001);
        postDetailEntity.setDescription(longDescription);

        // When
        Set<ConstraintViolation<PostDetailEntity>> violations = validator.validate(postDetailEntity);

        // Then
        assertThat(violations).hasSize(1);
        assertThat(violations.iterator().next().getMessage()).contains("Description must be between 1 and 5000 characters");
    }

    @Test
    @DisplayName("Description Validation - Should pass when description is exactly 5000 characters")
    void testValidation_DescriptionMaxLength(){
        // Given
        String maxLengthDescription = "A".repeat(5000);
        postDetailEntity.setDescription(maxLengthDescription);

        // When
        Set<ConstraintViolation<PostDetailEntity>> violations = validator.validate(postDetailEntity);

        // Then
        assertThat(violations).isEmpty();
    }
}
