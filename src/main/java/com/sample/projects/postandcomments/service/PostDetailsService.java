package com.sample.projects.postandcomments.service;

import com.sample.projects.postandcomments.entity.PostDetails;

import java.util.List;
import java.util.Optional;

public interface PostDetailsService {
    
    PostDetails save(PostDetails postDetails);
    
    Optional<PostDetails> findById(Long id);
    
    List<PostDetails> findAll();
    
    Optional<PostDetails> findByPostId(Long postId);
    
    PostDetails update(Long id, PostDetails postDetails);
    
    void deleteById(Long id);
    
    boolean existsById(Long id);

}

