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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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
        PostResponse createdPost = postService.save(request);

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
        PostResponse post = postService.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Post", id));
        
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
        List<PostResponse> posts = postService.findAll();
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
        
        PostResponse updatedPost = postService.update(id, request);

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
        postService.deleteById(id);
        CommonResponse<Object> response = ResponseUtil.buildSuccessResponse(
                HttpStatus.NO_CONTENT, "Post deleted successfully", null, httpRequest);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).body(response);
    }
}

