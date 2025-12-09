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
@RequestMapping("/api/posts")
public class PostController {

    private final PostService postService;
    private final AiService aiService;

    @Autowired
    public PostController(PostService postService,
                          AiService aiService) {
        this.postService = postService;
        this.aiService = aiService;
    }

    @PostMapping
    public ResponseEntity<CommonResponse<PostResponse>> createPost(
            @Valid @RequestBody PostRequest request,
            @RequestParam(name = "includeAi", defaultValue = "false") boolean includeAi,
            HttpServletRequest httpRequest) {
        log.info("Creating new post with title: {}, includeAi: {}", request.getTitle(), includeAi);
        PostResponse createdPost = postService.save(request);
        log.debug("Post created successfully with id: {}", createdPost.getId());

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

    @GetMapping("/{id}")
    public ResponseEntity<CommonResponse<PostResponse>> getPostById(
            @PathVariable Long id,
            @RequestParam(name = "includeAi", defaultValue = "false") boolean includeAi,
            HttpServletRequest httpRequest) {
        log.info("Retrieving post by id: {}, includeAi: {}", id, includeAi);
        PostResponse post = postService.findById(id)
                .orElseThrow(() -> {
                    log.warn("Post not found with id: {}", id);
                    return new ResourceNotFoundException("Post", id);
                });
        log.debug("Post retrieved successfully with id: {}", id);
        
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

    @GetMapping
    public ResponseEntity<CommonResponse<List<PostResponse>>> getAllPosts(HttpServletRequest httpRequest) {
        log.info("Retrieving all posts");
        List<PostResponse> posts = postService.findAll();
        log.debug("Retrieved {} posts", posts.size());
        CommonResponse<List<PostResponse>> response = ResponseUtil.buildSuccessResponse(
                HttpStatus.OK, Constants.POST_RETRIEVED_SUCCESSFULLY, posts, httpRequest);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/{id}")
    public ResponseEntity<CommonResponse<PostResponse>> updatePost(
            @PathVariable Long id,
            @RequestParam(name = "includeAi", defaultValue = "false") boolean includeAi,
            @Valid @RequestBody PostRequest request,
            HttpServletRequest httpRequest) {
        log.info("Updating post with id: {}, includeAi: {}", id, includeAi);
        PostResponse updatedPost = postService.update(id, request);
        log.debug("Post updated successfully with id: {}", id);

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

    @DeleteMapping("/{id}")
    public ResponseEntity<CommonResponse<Object>> deletePost(
            @PathVariable Long id,
            HttpServletRequest httpRequest) {
        log.info("Deleting post with id: {}", id);
        postService.deleteById(id);
        log.debug("Post deleted successfully with id: {}", id);
        CommonResponse<Object> response = ResponseUtil.buildSuccessResponse(
                HttpStatus.NO_CONTENT, "Post deleted successfully", null, httpRequest);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).body(response);
    }
}

