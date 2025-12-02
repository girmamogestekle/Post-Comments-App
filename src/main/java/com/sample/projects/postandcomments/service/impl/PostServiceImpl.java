package com.sample.projects.postandcomments.service.impl;

import com.sample.projects.postandcomments.dto.request.PostRequest;
import com.sample.projects.postandcomments.dto.response.PostResponse;
import com.sample.projects.postandcomments.entity.Post;
import com.sample.projects.postandcomments.entity.Tag;
import com.sample.projects.postandcomments.mapper.PostMapper;
import com.sample.projects.postandcomments.repository.PostRepository;
import com.sample.projects.postandcomments.service.PostService;
import com.sample.projects.postandcomments.service.TagService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
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
    public PostServiceImpl(PostRepository postRepository, PostMapper postMapper, TagService tagService) {
        this.postRepository = postRepository;
        this.postMapper = postMapper;
        this.tagService = tagService;
    }

    @Override
    public PostResponse save(PostRequest request) {
        Post post = postMapper.toEntity(request);
        
        // Set timestamps
        post.setCreatedAt(LocalDateTime.now());
        post.setUpdatedAt(LocalDateTime.now());
        
        // Handle tags if provided
        if (request.getTagIds() != null && !request.getTagIds().isEmpty()) {
            Set<Tag> tags = request.getTagIds().stream()
                    .map(tagId -> tagService.findById(tagId))
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
        Post existingPost = postRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Post not found with id: " + id));
        
        // Update title
        existingPost.setTitle(request.getTitle());
        existingPost.setUpdatedAt(LocalDateTime.now());
        
        // Handle tags if provided
        if (request.getTagIds() != null) {
            Set<Tag> tags = request.getTagIds().stream()
                    .map(tagId -> tagService.findById(tagId))
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
        if (!postRepository.existsById(id)) {
            throw new RuntimeException("Post not found with id: " + id);
        }
        postRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public boolean existsById(Long id) {
        return postRepository.existsById(id);
    }

}

