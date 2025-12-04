package com.sample.projects.postandcomments.service.impl;

import com.sample.projects.postandcomments.entity.PostDetails;
import com.sample.projects.postandcomments.repository.PostDetailsRepository;
import com.sample.projects.postandcomments.service.PostDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class PostDetailsServiceImpl implements PostDetailsService {

    private final PostDetailsRepository postDetailsRepository;

    @Autowired
    public PostDetailsServiceImpl(PostDetailsRepository postDetailsRepository) {
        this.postDetailsRepository = postDetailsRepository;
    }

    @Override
    public PostDetails save(PostDetails postDetails) {
        if (postDetails.getCreatedAt() == null) {
            postDetails.setCreatedAt(LocalDateTime.now());
        }
        postDetails.setUpdatedAt(LocalDateTime.now());
        return postDetailsRepository.save(postDetails);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<PostDetails> findById(Long id) {
        return postDetailsRepository.findById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public List<PostDetails> findAll() {
        return postDetailsRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<PostDetails> findByPostId(Long postId) {
        return postDetailsRepository.findAll().stream()
                .filter(details -> details.getPost() != null && details.getPost().getId().equals(postId))
                .findFirst();
    }

    @Override
    public PostDetails update(Long id, PostDetails postDetails) {
        return postDetailsRepository.findById(id)
                .map(existingDetails -> {
                    existingDetails.setDescription(postDetails.getDescription());
                    existingDetails.setUpdatedAt(LocalDateTime.now());
                    return postDetailsRepository.save(existingDetails);
                })
                .orElseThrow(() -> new RuntimeException("PostDetails not found with id: " + id));
    }

    @Override
    public void deleteById(Long id) {
        if (!postDetailsRepository.existsById(id)) {
            throw new RuntimeException("PostDetails not found with id: " + id);
        }
        postDetailsRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public boolean existsById(Long id) {
        return postDetailsRepository.existsById(id);
    }

}

