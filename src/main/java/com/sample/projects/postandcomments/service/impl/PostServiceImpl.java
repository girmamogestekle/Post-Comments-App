package com.sample.projects.postandcomments.service.impl;

import com.sample.projects.postandcomments.dto.request.PostRequest;
import com.sample.projects.postandcomments.dto.response.PostResponse;
import com.sample.projects.postandcomments.entity.Post;
import com.sample.projects.postandcomments.entity.Tag;
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
        log.debug("Saving new post with title: {}", request.getTitle());
        validateTagIds(request.getTagIds());
        
        Post post = postMapper.toEntity(request);
        
        // Set timestamps
        post.setCreatedAt(LocalDateTime.now());
        post.setUpdatedAt(LocalDateTime.now());
        
        // Handle tags if provided
        if (request.getTagIds() != null && !request.getTagIds().isEmpty()) {
            log.debug("Processing {} tag(s) for post", request.getTagIds().size());
            Set<Tag> tags = request.getTagIds().stream()
                    .map(tagService::findById)
                    .filter(Optional::isPresent)
                    .map(Optional::get)
                    .collect(Collectors.toCollection(LinkedHashSet::new));
            post.setTags(tags);
            log.debug("Associated {} tag(s) with post", tags.size());
        }
        
        Post savedPost = postRepository.save(post);
        log.info("Post saved successfully with id: {}", savedPost.getId());
        return postMapper.toResponse(savedPost);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<PostResponse> findById(Long id) {
        log.debug("Finding post by id: {}", id);
        if (id == null) {
            log.warn("Attempted to find post with null id");
            throw new ValidationException(Constants.POST_ID_CANNOT_BE_NULL);
        }
        Optional<PostResponse> result = postRepository.findById(id)
                .map(postMapper::toResponse);
        if (result.isPresent()) {
            log.debug("Post found with id: {}", id);
        } else {
            log.debug("Post not found with id: {}", id);
        }
        return result;
    }

    @Override
    @Transactional(readOnly = true)
    public List<PostResponse> findAll() {
        log.debug("Finding all posts");
        List<Post> posts = postRepository.findAll();
        log.info("Found {} post(s)", posts.size());
        return postMapper.toResponseList(posts);
    }

    @Override
    public PostResponse update(Long id, PostRequest request) {
        log.debug("Updating post with id: {}", id);
        if (id == null) {
            log.warn("Attempted to update post with null id");
            throw new ValidationException(Constants.POST_ID_CANNOT_BE_NULL);
        }
        
        validateTagIds(request.getTagIds());
        
        Post existingPost = postRepository.findById(id)
                .orElseThrow(() -> {
                    log.warn("Post not found for update with id: {}", id);
                    return new ResourceNotFoundException("Post", id);
                });
        
        // Update title
        log.debug("Updating post title from '{}' to '{}'", existingPost.getTitle(), request.getTitle());
        existingPost.setTitle(request.getTitle());
        existingPost.setUpdatedAt(LocalDateTime.now());
        
        // Handle tags if provided
        if (request.getTagIds() != null) {
            log.debug("Processing {} tag(s) for post update", request.getTagIds().size());
            Set<Tag> tags = request.getTagIds().stream()
                    .map(tagService::findById)
                    .filter(Optional::isPresent)
                    .map(Optional::get)
                    .collect(Collectors.toCollection(LinkedHashSet::new));
            existingPost.setTags(tags);
            log.debug("Associated {} tag(s) with updated post", tags.size());
        }
        
        Post updatedPost = postRepository.save(existingPost);
        log.info("Post updated successfully with id: {}", id);
        return postMapper.toResponse(updatedPost);
    }

    @Override
    public void deleteById(Long id) {
        log.debug("Deleting post with id: {}", id);
        if (id == null) {
            log.warn("Attempted to delete post with null id");
            throw new ValidationException(Constants.POST_ID_CANNOT_BE_NULL);
        }
        if (!postRepository.existsById(id)) {
            log.warn("Post not found for deletion with id: {}", id);
            throw new ResourceNotFoundException("Post", id);
        }
        postRepository.deleteById(id);
        log.info("Post deleted successfully with id: {}", id);
    }

    @Override
    @Transactional(readOnly = true)
    public boolean existsById(Long id) {
        log.debug("Checking if post exists with id: {}", id);
        if (id == null) {
            log.warn("Attempted to check existence with null id");
            throw new ValidationException(Constants.POST_ID_CANNOT_BE_NULL);
        }
        boolean exists = postRepository.existsById(id);
        log.debug("Post existence check for id {}: {}", id, exists);
        return exists;
    }
    
    private void validateTagIds(Set<Long> tagIds) {
        if (tagIds != null && !tagIds.isEmpty()) {
            log.debug("Validating {} tag id(s)", tagIds.size());
            List<String> errors = new ArrayList<>();
            for (Long tagId : tagIds) {
                if (tagId == null) {
                    log.warn("Null tag id found in validation");
                    errors.add("Tag id cannot be null");
                } else if (tagId <= 0) {
                    log.warn("Invalid tag id found: {}", tagId);
                    errors.add("Tag id must be greater than 0");
                } else if (tagService.findById(tagId).isEmpty()) {
                    log.warn("Tag not found with id: {}", tagId);
                    errors.add(String.format("Tag with id %d not found", tagId));
                }
            }
            if (!errors.isEmpty()) {
                log.error("Tag validation failed with {} error(s): {}", errors.size(), errors);
                throw new ValidationException("Invalid tag ids provided", errors);
            }
            log.debug("Tag validation passed for {} tag id(s)", tagIds.size());
        }
    }

}

