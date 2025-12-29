package com.sample.projects.postandcomments.service.impl;

import com.sample.projects.postandcomments.dto.request.PostRequest;
import com.sample.projects.postandcomments.dto.response.PostResponse;
import com.sample.projects.postandcomments.entity.PostEntity;
import com.sample.projects.postandcomments.entity.TagEntity;
import com.sample.projects.postandcomments.exception.ResourceNotFoundException;
import com.sample.projects.postandcomments.exception.ValidationException;
import com.sample.projects.postandcomments.mapper.PostMapper;
import com.sample.projects.postandcomments.util.Constants;
import com.sample.projects.postandcomments.repository.PostRepository;
import com.sample.projects.postandcomments.service.PostService;
import com.sample.projects.postandcomments.service.TagService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@Service
@Transactional
public class PostServiceImpl implements PostService {

    private final PostRepository postRepository;
    private final PostMapper postMapper;
    private final TagService tagService;

    @Autowired
    public PostServiceImpl(PostRepository postRepository,
                           PostMapper postMapper,
                           TagService tagService) {
        this.postRepository = postRepository;
        this.postMapper = postMapper;
        this.tagService = tagService;
    }

    @Override
    public PostResponse save(PostRequest request) {
        log.debug("Saving new postEntity with title: {}", request.getTitle());
        validateTagIds(request.getTagIds());
        
        PostEntity postEntity = postMapper.toEntity(request);
        
        // Set timestamps
        postEntity.setCreatedAt(LocalDateTime.now());

        // Handle tagEntities if provided
        if (request.getTagIds() != null && !request.getTagIds().isEmpty()) {
            log.debug("Processing {} tag(s) for postEntity", request.getTagIds().size());
            Set<TagEntity> tagEntities = request.getTagIds().stream()
                    .map(tagService::findById)
                    .filter(Optional::isPresent)
                    .map(Optional::get)
                    .collect(Collectors.toCollection(LinkedHashSet::new));
            postEntity.setTagEntities(tagEntities);
            log.debug("Associated {} tag(s) with postEntity", tagEntities.size());
        }
        
        PostEntity savedPostEntity = postRepository.save(postEntity);
        log.info("PostEntity saved successfully with id: {}", savedPostEntity.getId());
        return postMapper.toPostResponse(savedPostEntity);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<PostResponse> findById(Long id) {
        log.debug("Finding postEntity by id: {}", id);
        if (id == null) {
            log.warn("Attempted to find postEntity with null id");
            throw new ValidationException(Constants.POST_ID_CANNOT_BE_NULL);
        }
        Optional<PostResponse> result = postRepository.findById(id)
                .map(postMapper::toPostResponse);
        if (result.isPresent()) {
            log.debug("PostEntity found with id: {}", id);
        } else {
            log.debug("PostEntity not found with id: {}", id);
        }
        return result;
    }

    @Override
    @Transactional(readOnly = true)
    public List<PostResponse> findAll() {
        log.debug("Finding all postEntities");
        List<PostEntity> postEntities = postRepository.findAll();
        log.info("Found {} postEntity(s)", postEntities.size());
        return postMapper.toResponseList(postEntities);
    }

    @Override
    public PostResponse update(Long id, PostRequest request) {
        log.debug("Updating postEntity with id: {}", id);
        if (id == null) {
            log.warn("Attempted to update postEntity with null id");
            throw new ValidationException(Constants.POST_ID_CANNOT_BE_NULL);
        }
        
        validateTagIds(request.getTagIds());
        
        PostEntity existingPostEntity = postRepository.findById(id)
                .orElseThrow(() -> {
                    log.warn("PostEntity not found for update with id: {}", id);
                    return new ResourceNotFoundException("PostEntity", id);
                });
        
        // Update title
        log.debug("Updating postEntity title from '{}' to '{}'", existingPostEntity.getTitle(), request.getTitle());
        existingPostEntity.setTitle(request.getTitle());
        existingPostEntity.setUpdatedAt(LocalDateTime.now());
        
        // Handle tagEntities if provided
        if (request.getTagIds() != null) {
            log.debug("Processing {} tag(s) for postEntity update", request.getTagIds().size());
            Set<TagEntity> tagEntities = request.getTagIds().stream()
                    .map(tagService::findById)
                    .filter(Optional::isPresent)
                    .map(Optional::get)
                    .collect(Collectors.toCollection(LinkedHashSet::new));
            existingPostEntity.setTagEntities(tagEntities);
            log.debug("Associated {} tag(s) with updated postEntity", tagEntities.size());
        }
        
        PostEntity updatedPostEntity = postRepository.save(existingPostEntity);
        log.info("PostEntity updated successfully with id: {}", id);
        return postMapper.toPostResponse(updatedPostEntity);
    }

    @Override
    public void deleteById(Long id) {
        log.debug("Deleting postEntity with id: {}", id);
        if (id == null) {
            log.warn("Attempted to delete postEntity with null id");
            throw new ValidationException(Constants.POST_ID_CANNOT_BE_NULL);
        }
        if (!postRepository.existsById(id)) {
            log.warn("PostEntity not found for deletion with id: {}", id);
            throw new ResourceNotFoundException("PostEntity", id);
        }
        postRepository.deleteById(id);
        log.info("PostEntity deleted successfully with id: {}", id);
    }

    @Override
    @Transactional(readOnly = true)
    public boolean existsById(Long id) {
        log.debug("Checking if postEntity exists with id: {}", id);
        if (id == null) {
            log.warn("Attempted to check existence with null id");
            throw new ValidationException(Constants.POST_ID_CANNOT_BE_NULL);
        }
        boolean exists = postRepository.existsById(id);
        log.debug("PostEntity existence check for id {}: {}", id, exists);
        return exists;
    }
    
    private void validateTagIds(Set<Long> tagIds) {
        if (tagIds != null && !tagIds.isEmpty()) {
            log.debug("Validating {} tag id(s)", tagIds.size());
            List<String> errors = new ArrayList<>();
            for (Long tagId : tagIds) {
                if (tagId == null) {
                    log.warn("Null tag id found in validation");
                    errors.add("TagEntity id cannot be null");
                } else if (tagId <= 0) {
                    log.warn("Invalid tag id found: {}", tagId);
                    errors.add("TagEntity id must be greater than 0");
                } else if (tagService.findById(tagId).isEmpty()) {
                    log.warn("TagEntity not found with id: {}", tagId);
                    errors.add(String.format("TagEntity with id %d not found", tagId));
                }
            }
            if (!errors.isEmpty()) {
                log.error("TagEntity validation failed with {} error(s): {}", errors.size(), errors);
                throw new ValidationException("Invalid tag ids provided", errors);
            }
            log.debug("TagEntity validation passed for {} tag id(s)", tagIds.size());
        }
    }

}

