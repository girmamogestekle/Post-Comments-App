package com.sample.projects.postandcomments.service.impl;

import com.sample.projects.postandcomments.dto.request.PostDetailRequest;
import com.sample.projects.postandcomments.dto.response.PostDetailResponse;
import com.sample.projects.postandcomments.entity.PostDetailEntity;
import com.sample.projects.postandcomments.exception.ResourceNotFoundException;
import com.sample.projects.postandcomments.exception.ValidationException;
import com.sample.projects.postandcomments.mapper.PostDetailMapper;
import com.sample.projects.postandcomments.repository.PostDetailRepository;
import com.sample.projects.postandcomments.service.PostDetailsService;
import com.sample.projects.postandcomments.util.Constants;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@Transactional
public class PostDetailsServiceImpl implements PostDetailsService {

    private final PostDetailRepository postDetailRepository;
    private final PostDetailMapper postDetailMapper;

    @Autowired
    public PostDetailsServiceImpl(PostDetailRepository postDetailRepository, PostDetailMapper postDetailMapper) {
        this.postDetailRepository = postDetailRepository;
        this.postDetailMapper = postDetailMapper;
    }

    @Override
    public PostDetailResponse save(PostDetailRequest postDetailRequest) {
        log.debug("Saving New Post Detail: {}", postDetailRequest.toString());
        PostDetailEntity postDetailEntity = postDetailMapper.toPostDetailEntity(postDetailRequest);

        // Set Timestamps
        postDetailEntity.setCreatedAt(LocalDateTime.now());

        PostDetailEntity postDetailEntitySaved = postDetailRepository.save(postDetailEntity);
        log.info("Post Detail Entity Saved Successfully With Id: {}", postDetailEntitySaved.getId());

        return postDetailMapper.toPostDetailResponse(postDetailEntitySaved);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<PostDetailResponse> findById(Long id) {
        log.debug("Finding Post Detail Entity By Id: {}", id);
        if(id == null) {
            log.warn("Attempt To Find Post Detail Entity With Null Id");
            throw new ValidationException(Constants.POST_DETAIL_ID_CANNOT_BE_NULL);
        }
        Optional<PostDetailResponse> result = postDetailRepository.findById(id)
                .map(postDetailMapper::toPostDetailResponse);
        if(result.isEmpty()) {
            log.warn("Attempt To Find Post Detail Entity With Null Id");
        }
        return result;
    }

    @Override
    @Transactional(readOnly = true)
    public List<PostDetailResponse> findAll() {
        log.debug("Finding All Post Details Entities");
        List<PostDetailEntity> allPostDetailEntities = postDetailRepository.findAll();
        log.info("Found {} Post Detail Entities", allPostDetailEntities.size());
        return postDetailMapper.toPostDetailResponses(allPostDetailEntities);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<PostDetailResponse> findByPostId(Long postId) {
        log.info("Retrieving Post Detail Entity By Post Id: {}", postId);
        Optional<PostDetailResponse> postDetailResponse = postDetailRepository.findByPostEntity_Id(postId)
                .map(postDetailMapper::toPostDetailResponse);
        if(postDetailResponse.isEmpty()) {
            log.debug("Attempt To Find Post Detail Entity With Null Post Id: {}", postId);
        }
        return postDetailResponse;
    }

    @Override
    public PostDetailResponse update(Long id, PostDetailRequest postDetailRequest) {
        log.debug("Updating Post Detail With Id: {}", id);
        if(id == null) {
            log.warn("Attempted To Update Post Detail Entity With Null Id");
            throw new ValidationException(Constants.POST_DETAIL_ID_CANNOT_BE_NULL);
        }

        PostDetailEntity existingPostDetailEntity = postDetailRepository.findById(id)
                .orElseThrow(() -> {
                    log.warn("Attempt To Update Post Detail Entity With Null Id");
                    return new ResourceNotFoundException("Post Detail Entity", id);
                });

        // Update description
        log.debug("Updating Post Detail Entity Description From: {} to {}", existingPostDetailEntity.getDescription(), postDetailRequest.getDescription());
        existingPostDetailEntity.setDescription(postDetailRequest.getDescription());
        existingPostDetailEntity.setUpdatedAt(LocalDateTime.now());
        PostDetailEntity postDetailEntityUpdated = postDetailRepository.save(existingPostDetailEntity);
        log.info("Post Detail Entity Updated Successfully With Id: {}", postDetailEntityUpdated.getId());
        return postDetailMapper.toPostDetailResponse(postDetailEntityUpdated);
    }

    @Override
    public void deleteById(Long id) {
        log.debug("Deleting Post Detail Entity With Id: {}", id);
        if(id == null) {
            log.warn("Attempt To Delete Post Detail Entity With Null Id: {}", id);
            throw new ValidationException(Constants.POST_DETAIL_ID_CANNOT_BE_NULL);
        }
        if (!postDetailRepository.existsById(id)) {
            log.warn("Attempt To Delete Post Detail Entity With Null Id: {}", id);
            throw new ResourceNotFoundException("Post Detail Entity" + id);
        }
        postDetailRepository.deleteById(id);
        log.info("Post Detail Entity Deleted Successfully With Id: {}", id);
    }


}

