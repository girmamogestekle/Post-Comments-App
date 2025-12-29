package com.sample.projects.postandcomments.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sample.projects.postandcomments.dto.request.PostRequest;
import com.sample.projects.postandcomments.dto.response.PostResponse;
import com.sample.projects.postandcomments.service.AiService;
import com.sample.projects.postandcomments.service.PostService;
import com.sample.projects.postandcomments.util.Constants;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@Slf4j
@WebMvcTest(controllers = PostController.class)
@DisplayName("PostController API Integration Tests")
class PostEntityControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private PostService postService;

    @MockitoBean
    private AiService aiService;

    private PostRequest postRequest;
    private PostResponse postResponse;

    @BeforeEach
    void setUp() {
        log.debug("Setting up test data for PostEntityControllerTest");
        postRequest = PostRequest.builder()
                .title("Test PostEntity Title")
                .build();

        postResponse = PostResponse.builder()
                .id(1L)
                .title("Test PostEntity Title")
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();
        log.debug("Test data setup completed");
    }

    @Test
    @DisplayName("POST /api/v1/postEntities - Should create postEntity successfully")
    void testCreatePost_Success() throws Exception {
        when(postService.save(any(PostRequest.class))).thenReturn(postResponse);

        mockMvc.perform(post("/api/v1/post/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(postRequest)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.status").value(201))
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.message").value(Constants.POST_CREATED_SUCCESSFULLY))
                .andExpect(jsonPath("$.payload.id").value(1L))
                .andExpect(jsonPath("$.payload.title").value("Test PostEntity Title"));

        verify(postService).save(any(PostRequest.class));
        verify(aiService, never()).explainPost(any(PostResponse.class));
    }

    @Test
    @DisplayName("POST /api/v1/postEntities?includeAi=true - Should create postEntity with AI response")
    void testCreatePost_WithAiResponse() throws Exception {
        when(postService.save(any(PostRequest.class))).thenReturn(postResponse);

        mockMvc.perform(post("/api/v1/post/create")
                        .param("includeAi", "true")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(postRequest)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.status").value(201))
                .andExpect(jsonPath("$.success").value(true));

        verify(postService).save(any(PostRequest.class));
    }

    @Test
    @DisplayName("POST /api/v1/postEntities - Should return 400 for invalid request")
    void testCreatePost_ValidationError() throws Exception {
        PostRequest invalidRequest = PostRequest.builder()
                .title("") // Empty title should fail validation
                .build();

        mockMvc.perform(post("/api/v1/post/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidRequest)))
                .andExpect(status().isBadRequest());

        verify(postService, never()).save(any(PostRequest.class));
    }

    @Test
    @DisplayName("GET /api/v1/postEntities/{id} - Should return postEntity by id")
    void testGetPostById_Success() throws Exception {
        when(postService.findById(1L)).thenReturn(Optional.of(postResponse));

        mockMvc.perform(get("/api/v1/post/get/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value(200))
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.message").value(Constants.POST_RETRIEVED_SUCCESSFULLY))
                .andExpect(jsonPath("$.payload.id").value(1L))
                .andExpect(jsonPath("$.payload.title").value("Test PostEntity Title"));

        verify(postService).findById(1L);
    }

    @Test
    @DisplayName("GET /api/v1/postEntities/{id} - Should return 404 when postEntity not found")
    void testGetPostById_NotFound() throws Exception {
        when(postService.findById(999L)).thenReturn(Optional.empty());

        mockMvc.perform(get("/api/v1/post/get/999"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value(404))
                .andExpect(jsonPath("$.success").value(false))
                .andExpect(jsonPath("$.message").value("PostEntity with id 999 not found"))
                .andExpect(jsonPath("$.errors[0]").value("PostEntity with id 999 not found"));

        verify(postService).findById(999L);
    }

    @Test
    @DisplayName("GET /api/v1/postEntities - Should return all postEntities")
    void testGetAllPosts_Success() throws Exception {
        List<PostResponse> posts = Arrays.asList(
                postResponse,
                PostResponse.builder()
                        .id(2L)
                        .title("Second PostEntity")
                        .createdAt(LocalDateTime.now())
                        .updatedAt(LocalDateTime.now())
                        .build()
        );

        when(postService.findAll()).thenReturn(posts);

        mockMvc.perform(get("/api/v1/post/get/all"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value(200))
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.message").value(Constants.POST_RETRIEVED_SUCCESSFULLY))
                .andExpect(jsonPath("$.payload").isArray())
                .andExpect(jsonPath("$.payload.length()").value(2))
                .andExpect(jsonPath("$.payload[0].id").value(1L))
                .andExpect(jsonPath("$.payload[1].id").value(2L));

        verify(postService).findAll();
    }

    @Test
    @DisplayName("PUT /api/v1/postEntities/{id} - Should update postEntity successfully")
    void testUpdatePost_Success() throws Exception {
        PostRequest updateRequest = PostRequest.builder()
                .title("Updated PostEntity Title")
                .build();

        PostResponse updatedResponse = PostResponse.builder()
                .id(1L)
                .title("Updated PostEntity Title")
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();

        when(postService.update(eq(1L), any(PostRequest.class))).thenReturn(updatedResponse);

        mockMvc.perform(put("/api/v1/post/update/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value(200))
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.message").value(Constants.POST_UPDATED_SUCCESSFULLY))
                .andExpect(jsonPath("$.payload.title").value("Updated PostEntity Title"));

        verify(postService).update(eq(1L), any(PostRequest.class));
    }

    @Test
    @DisplayName("PUT /api/v1/postEntities/{id} - Should return 404 when postEntity not found")
    void testUpdatePost_NotFound() throws Exception {
        PostRequest updateRequest = PostRequest.builder()
                .title("Updated PostEntity Title")
                .build();

        when(postService.update(eq(999L), any(PostRequest.class)))
                .thenThrow(new com.sample.projects.postandcomments.exception.ResourceNotFoundException("PostEntity", 999L));

        mockMvc.perform(put("/api/v1/post/update/999")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateRequest)))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value(404))
                .andExpect(jsonPath("$.success").value(false))
                .andExpect(jsonPath("$.message").value("PostEntity with id 999 not found"));

        verify(postService).update(eq(999L), any(PostRequest.class));
    }

    @Test
    @DisplayName("DELETE /api/v1/postEntities/{id} - Should delete postEntity successfully")
    void testDeletePost_Success() throws Exception {
        doNothing().when(postService).deleteById(1L);

        mockMvc.perform(delete("/api/v1/post/delete/1"))
                .andExpect(status().isNoContent())
                .andExpect(jsonPath("$.status").value(204))
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.message").value("PostEntity deleted successfully"));

        verify(postService).deleteById(1L);
    }

    @Test
    @DisplayName("DELETE /api/v1/postEntities/{id} - Should return 404 when postEntity not found")
    void testDeletePost_NotFound() throws Exception {
        doThrow(new com.sample.projects.postandcomments.exception.ResourceNotFoundException("PostEntity", 999L))
                .when(postService).deleteById(999L);

        mockMvc.perform(delete("/api/v1/post/delete/999"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value(404))
                .andExpect(jsonPath("$.success").value(false))
                .andExpect(jsonPath("$.message").value("PostEntity with id 999 not found"));

        verify(postService).deleteById(999L);
    }
}
