package com.sample.projects.postandcomments.entity;

import com.sample.projects.postandcomments.config.TestJpaAuditingConfig;
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

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

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

    }
}
