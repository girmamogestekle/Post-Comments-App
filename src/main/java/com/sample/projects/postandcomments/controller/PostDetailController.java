package com.sample.projects.postandcomments.controller;

import com.sample.projects.postandcomments.dto.CommonResponse;
import com.sample.projects.postandcomments.dto.request.PostDetailRequest;
import com.sample.projects.postandcomments.dto.response.PostDetailResponse;
import com.sample.projects.postandcomments.exception.ResourceNotFoundException;
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
    public ResponseEntity<CommonResponse<PostDetailResponse>> getPostDetailById(@PathVariable Long id, HttpServletRequest httpServletRequest) {
        log.info("Retrieving Post Detail By Id: {}", id);
        PostDetailResponse postDetailResponse = postDetailService.findById(id)
                .orElseThrow(() -> {
                    log.warn("Post Detail By Id not found: {}", id);
                    return new ResourceNotFoundException("Post Detail", id);
                });
        log.debug("Post Detail Found Successfully With Id: {}", postDetailResponse.getId());
        CommonResponse<PostDetailResponse> commonPostDetailResponse = ResponseUtil.buildSuccessResponse(
                HttpStatus.OK, Constants.POST_DETAIL_RETRIEVED_SUCCESSFULLY, postDetailResponse, httpServletRequest);
        return ResponseEntity.ok(commonPostDetailResponse);
    }

    @GetMapping(name = "Get Post Details", value = "/get/all")
    public ResponseEntity<CommonResponse<List<PostDetailResponse>>> getAllPostDetail(HttpServletRequest httpServletRequestRequest) {
        log.info("Retrieving All Post Details");
        List<PostDetailResponse> allPostDetails = postDetailService.findAll();
        log.debug("Retrieved {} Post Details", allPostDetails.size());
        CommonResponse<List<PostDetailResponse>> response = ResponseUtil.buildSuccessResponse(
                HttpStatus.OK, Constants.POST_DETAIL_RETRIEVED_SUCCESSFULLY, allPostDetails, httpServletRequestRequest);
        return ResponseEntity.ok(response);
    }

    @GetMapping(name = "Get Post Detail By Post", value = "/get/post/{postId}")
    public ResponseEntity<CommonResponse<PostDetailResponse>> getPostDetailByPostId(@PathVariable Long postId,  HttpServletRequest httpServletRequest) {
        log.info("Retrieving Post Detail By Post Id: {}", postId);
        PostDetailResponse postDetailResponse = postDetailService.findByPostId(postId)
                .orElseThrow(() -> {
                    log.warn("Post Detail Not Found With Post Id: {}", postId);
                    return new ResourceNotFoundException("Post Detail With PostId", postId);
                });
        log.debug("Post Detail Retrieved Successfully With PostId: {}", postId);

        CommonResponse<PostDetailResponse> commonPostDetailResponse = ResponseUtil.buildSuccessResponse(
                HttpStatus.OK, Constants.POST_RETRIEVED_SUCCESSFULLY, postDetailResponse, httpServletRequest
        );
        return ResponseEntity.ok(commonPostDetailResponse);
    }

    @PutMapping(name = "Update Post Detail", value = "/update/{id}")
    public ResponseEntity<CommonResponse<PostDetailResponse>> updatePostDetail(@PathVariable Long id,
                                                             @RequestBody PostDetailRequest postDetailRequest,
                                                             HttpServletRequest httpServletRequest) {
        log.info("Updating Post Detail With Id: {}", id);
        PostDetailResponse updatedPostDetail = postDetailService.update(id, postDetailRequest);
        log.debug("Post Detail Updated Successfully With Id: {}", updatedPostDetail.getId());

        CommonResponse<PostDetailResponse> commonPostDetailResponse = ResponseUtil.buildSuccessResponse(
                HttpStatus.OK, Constants.POST_DETAIL_UPDATED_SUCCESSFULLY, updatedPostDetail, httpServletRequest
        );

        return ResponseEntity.ok(commonPostDetailResponse);
    }

    @DeleteMapping(name = "Delete Post Detail", value = "/delete/{id}")
    public ResponseEntity<CommonResponse<Object>> deletePostDetail(@PathVariable Long id,  HttpServletRequest httpServletRequest) {
        log.info("Deleting Post Detail With Id: {}", id);
        postDetailService.deleteById(id);
        log.debug("Post Detail Deleted Successfully With Id: {}", id);
        CommonResponse<Object> commonPostDetailResponse = ResponseUtil.buildSuccessResponse(
          HttpStatus.NO_CONTENT, Constants.POST_DETAIL_DELETED_SUCCESSFULLY, null, httpServletRequest
        );
        return ResponseEntity.status(HttpStatus.NO_CONTENT).body(commonPostDetailResponse);
    }
}

