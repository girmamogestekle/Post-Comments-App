package com.sample.projects.postandcomments.service.impl;

import com.sample.projects.postandcomments.dto.request.PostDetailRequest;
import com.sample.projects.postandcomments.dto.response.PostDetailResponse;
import com.sample.projects.postandcomments.entity.PostDetailEntity;
import com.sample.projects.postandcomments.mapper.PostDetailMapper;
import com.sample.projects.postandcomments.repository.PostDetailRepository;
import com.sample.projects.postandcomments.service.PostDetailsService;
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
    public Optional<PostDetailEntity> findById(Long id) {
        return postDetailRepository.findById(id);
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
    public Optional<PostDetailEntity> findByPostId(Long postId) {
        return postDetailRepository.findAll().stream()
                .filter(details -> details.getPostEntity() != null && details.getPostEntity().getId().equals(postId))
                .findFirst();
    }

    @Override
    public PostDetailEntity update(Long id, PostDetailEntity postDetailsEntity) {
        return postDetailRepository.findById(id)
                .map(existingDetails -> {
                    existingDetails.setDescription(postDetailsEntity.getDescription());
                    existingDetails.setUpdatedAt(LocalDateTime.now());
                    return postDetailRepository.save(existingDetails);
                })
                .orElseThrow(() -> new RuntimeException("PostDetailEntity not found with id: " + id));
    }

    @Override
    public void deleteById(Long id) {
        if (!postDetailRepository.existsById(id)) {
            throw new RuntimeException("PostDetailEntity not found with id: " + id);
        }
        postDetailRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public boolean existsById(Long id) {
        return postDetailRepository.existsById(id);
    }

}

