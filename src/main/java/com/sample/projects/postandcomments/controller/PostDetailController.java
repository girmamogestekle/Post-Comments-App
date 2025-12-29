package com.sample.projects.postandcomments.controller;

import com.sample.projects.postandcomments.dto.CommonResponse;
import com.sample.projects.postandcomments.dto.request.PostDetailRequest;
import com.sample.projects.postandcomments.dto.response.PostDetailResponse;
import com.sample.projects.postandcomments.entity.PostDetailEntity;
import com.sample.projects.postandcomments.service.PostDetailsService;
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
@RequestMapping(name = "Post Detail Controller", value = "/api/v1/post-detail")
public class PostDetailController {

    private final PostDetailsService postDetailService;

    @Autowired
    public PostDetailController(PostDetailsService postDetailService) {
        this.postDetailService = postDetailService;
    }

    @PostMapping(name = "Create Post Detail", value = "/create")
    public ResponseEntity<CommonResponse<PostDetailResponse>> createPostDetail(
            @Valid @RequestBody PostDetailRequest postDetailRequest,
            HttpServletRequest httpServletRequest) {
        log.info("Creating New Post Detail: {}", postDetailRequest);
        PostDetailResponse createdPostDetail = postDetailService.save(postDetailRequest);
        log.debug("Post Detail Created Successfully With Id: {}", createdPostDetail.getId());

        CommonResponse<PostDetailResponse> postDetailResponse = ResponseUtil.buildSuccessResponse(
                HttpStatus.CREATED, Constants.POST_DETAIL_CREATED_SUCCESSFULLY, createdPostDetail, httpServletRequest);

        return ResponseEntity.status(HttpStatus.CREATED).body(postDetailResponse);
    }

    @GetMapping(name = "Get Post Detail", value = "/get/{id}")
    public ResponseEntity<PostDetailEntity> getPostDetailsById(@PathVariable Long id) {
        return postDetailService.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping(name = "Get Post Details", value = "/get/all")
    public ResponseEntity<CommonResponse<List<PostDetailResponse>>> getAllPostDetails(HttpServletRequest httpRequest) {
        log.info("Retrieving All Post Details");
        List<PostDetailResponse> allPostDetails = postDetailService.findAll();
        log.debug("Retrieved {} Post Details", allPostDetails.size());
        CommonResponse<List<PostDetailResponse>> response = ResponseUtil.buildSuccessResponse(
                HttpStatus.OK, Constants.POST_DETAIL_RETRIEVED_SUCCESSFULLY, allPostDetails, httpRequest);
        return ResponseEntity.ok(response);
    }

    @GetMapping(name = "Get Post Detail By Post", value = "/get/post/{postId}")
    public ResponseEntity<PostDetailEntity> getPostDetailsByPostId(@PathVariable Long postId) {
        return postDetailService.findByPostId(postId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping(name = "Update Post Detail", value = "/update/{id}")
    public ResponseEntity<PostDetailEntity> updatePostDetails(@PathVariable Long id, @RequestBody PostDetailEntity postDetailsEntity) {
        if (!postDetailService.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        PostDetailEntity updatedDetails = postDetailService.update(id, postDetailsEntity);
        return ResponseEntity.ok(updatedDetails);
    }

    @DeleteMapping(name = "Delete Post Detail", value = "/delete/{id}")
    public ResponseEntity<Void> deletePostDetails(@PathVariable Long id) {
        if (!postDetailService.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        postDetailService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}

