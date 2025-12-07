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
        validateTagIds(request.getTagIds());
        
        Post post = postMapper.toEntity(request);
        
        // Set timestamps
        post.setCreatedAt(LocalDateTime.now());
        post.setUpdatedAt(LocalDateTime.now());
        
        // Handle tags if provided
        if (request.getTagIds() != null && !request.getTagIds().isEmpty()) {
            Set<Tag> tags = request.getTagIds().stream()
                    .map(tagService::findById)
                    .filter(Optional::isPresent)
                    .map(Optional::get)
                    .collect(Collectors.toCollection(LinkedHashSet::new));
            post.setTags(tags);
        }
        
        Post savedPost = postRepository.save(post);
        return postMapper.toResponse(savedPost);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<PostResponse> findById(Long id) {
        if (id == null) {
            throw new ValidationException(Constants.POST_ID_CANNOT_BE_NULL);
        }
        return postRepository.findById(id)
                .map(postMapper::toResponse);
    }

    @Override
    @Transactional(readOnly = true)
    public List<PostResponse> findAll() {
        List<Post> posts = postRepository.findAll();
        return postMapper.toResponseList(posts);
    }

    @Override
    public PostResponse update(Long id, PostRequest request) {
        if (id == null) {
            throw new ValidationException(Constants.POST_ID_CANNOT_BE_NULL);
        }
        
        validateTagIds(request.getTagIds());
        
        Post existingPost = postRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Post", id));
        
        // Update title
        existingPost.setTitle(request.getTitle());
        existingPost.setUpdatedAt(LocalDateTime.now());
        
        // Handle tags if provided
        if (request.getTagIds() != null) {
            Set<Tag> tags = request.getTagIds().stream()
                    .map(tagService::findById)
                    .filter(Optional::isPresent)
                    .map(Optional::get)
                    .collect(Collectors.toCollection(LinkedHashSet::new));
            existingPost.setTags(tags);
        }
        
        Post updatedPost = postRepository.save(existingPost);
        return postMapper.toResponse(updatedPost);
    }

    @Override
    public void deleteById(Long id) {
        if (id == null) {
            throw new ValidationException(Constants.POST_ID_CANNOT_BE_NULL);
        }
        if (!postRepository.existsById(id)) {
            throw new ResourceNotFoundException("Post", id);
        }
        postRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public boolean existsById(Long id) {
        if (id == null) {
            throw new ValidationException(Constants.POST_ID_CANNOT_BE_NULL);
        }
        return postRepository.existsById(id);
    }
    
    private void validateTagIds(Set<Long> tagIds) {
        if (tagIds != null && !tagIds.isEmpty()) {
            List<String> errors = new ArrayList<>();
            for (Long tagId : tagIds) {
                if (tagId == null) {
                    errors.add("Tag id cannot be null");
                } else if (tagId <= 0) {
                    errors.add("Tag id must be greater than 0");
                } else if (tagService.findById(tagId).isEmpty()) {
                    errors.add(String.format("Tag with id %d not found", tagId));
                }
            }
            if (!errors.isEmpty()) {
                throw new ValidationException("Invalid tag ids provided", errors);
            }
        }
    }

}

