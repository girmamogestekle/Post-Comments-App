package com.sample.projects.postandcomments.controller;

import com.sample.projects.postandcomments.dto.CommonResponse;
import com.sample.projects.postandcomments.dto.request.PostRequest;
import com.sample.projects.postandcomments.dto.response.PostResponse;
import com.sample.projects.postandcomments.exception.ResourceNotFoundException;
import com.sample.projects.postandcomments.service.AiService;
import com.sample.projects.postandcomments.service.PostService;
import com.sample.projects.postandcomments.util.Constants;
import com.sample.projects.postandcomments.util.ResponseUtil;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping(name  = "Post Controller", value = "/api/v1/post")
public class PostController {

    private final PostService postService;
    private final AiService aiService;

    @Autowired
    public PostController(PostService postService,
                          AiService aiService) {
        this.postService = postService;
        this.aiService = aiService;
    }

    @PostMapping(name = "Create Post", value = "/create")
    public ResponseEntity<CommonResponse<PostResponse>> createPost(
            @Valid @RequestBody PostRequest request,
            @RequestParam(name = "includeAi", defaultValue = "false") boolean includeAi,
            HttpServletRequest httpRequest) {
        log.info("Creating new postEntity with title: {}, includeAi: {}", request.getTitle(), includeAi);
        PostResponse createdPost = postService.save(request);
        log.debug("PostEntity created successfully with id: {}", createdPost.getId());

        CommonResponse<PostResponse> response;

        if(includeAi){
            response = ResponseUtil.buildSuccessResponseWithAiResponse(
                    HttpStatus.CREATED, Constants.POST_CREATED_SUCCESSFULLY, createdPost, aiService.explainPost(createdPost), httpRequest);
        }
        else{
            response = ResponseUtil.buildSuccessResponse(
                    HttpStatus.CREATED, Constants.POST_CREATED_SUCCESSFULLY, createdPost, httpRequest);
        }

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping(name = "Get Post", value = "/get/{id}")
    public ResponseEntity<CommonResponse<PostResponse>> getPostById(
            @PathVariable Long id,
            @RequestParam(name = "includeAi", defaultValue = "false") boolean includeAi,
            HttpServletRequest httpRequest) {
        log.info("Retrieving postEntity by id: {}, includeAi: {}", id, includeAi);
        PostResponse post = postService.findById(id)
                .orElseThrow(() -> {
                    log.warn("PostEntity not found with id: {}", id);
                    return new ResourceNotFoundException("PostEntity", id);
                });
        log.debug("PostEntity retrieved successfully with id: {}", id);
        
        CommonResponse<PostResponse> response;
        if(includeAi){
            response = ResponseUtil.buildSuccessResponseWithAiResponse(
                    HttpStatus.OK, Constants.POST_RETRIEVED_SUCCESSFULLY, post, aiService.explainPost(post), httpRequest);
        } else{
            response = ResponseUtil.buildSuccessResponse(
                    HttpStatus.OK, Constants.POST_RETRIEVED_SUCCESSFULLY, post, httpRequest);
        }
        return ResponseEntity.ok(response);
    }

    @GetMapping(name = "Get Posts", value = "/get/all")
    public ResponseEntity<CommonResponse<List<PostResponse>>> getAllPosts(HttpServletRequest httpRequest) {
        log.info("Retrieving all postEntities");
        List<PostResponse> posts = postService.findAll();
        log.debug("Retrieved {} postEntities", posts.size());
        CommonResponse<List<PostResponse>> response = ResponseUtil.buildSuccessResponse(
                HttpStatus.OK, Constants.POST_RETRIEVED_SUCCESSFULLY, posts, httpRequest);
        return ResponseEntity.ok(response);
    }

    @PutMapping(name = "Update Post", value = "/update/{id}")
    public ResponseEntity<CommonResponse<PostResponse>> updatePost(
            @PathVariable Long id,
            @RequestParam(name = "includeAi", defaultValue = "false") boolean includeAi,
            @Valid @RequestBody PostRequest request,
            HttpServletRequest httpRequest) {
        log.info("Updating postEntity with id: {}, includeAi: {}", id, includeAi);
        PostResponse updatedPost = postService.update(id, request);
        log.debug("PostEntity updated successfully with id: {}", id);

        CommonResponse<PostResponse> response;
        if(includeAi){
            response = ResponseUtil.buildSuccessResponseWithAiResponse(
                    HttpStatus.OK, Constants.POST_UPDATED_SUCCESSFULLY, updatedPost, aiService.explainPost(updatedPost), httpRequest);
        } else{
            response = ResponseUtil.buildSuccessResponse(
                    HttpStatus.OK, Constants.POST_UPDATED_SUCCESSFULLY, updatedPost, httpRequest);
        }

        return ResponseEntity.ok(response);
    }

    @DeleteMapping(name = "Delete Post", value = "/delete/{id}")
    public ResponseEntity<CommonResponse<Object>> deletePost(
            @PathVariable Long id,
            HttpServletRequest httpRequest) {
        log.info("Deleting postEntity with id: {}", id);
        postService.deleteById(id);
        log.debug("PostEntity deleted successfully with id: {}", id);
        CommonResponse<Object> response = ResponseUtil.buildSuccessResponse(
                HttpStatus.NO_CONTENT, "PostEntity deleted successfully", null, httpRequest);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).body(response);
    }
}

