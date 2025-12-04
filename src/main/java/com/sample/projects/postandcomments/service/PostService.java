package com.sample.projects.postandcomments.service;

import com.sample.projects.postandcomments.dto.request.PostRequest;
import com.sample.projects.postandcomments.dto.response.PostResponse;

import java.util.List;
import java.util.Optional;

public interface PostService {
    
    PostResponse save(PostRequest request);
    
    Optional<PostResponse> findById(Long id);
    
    List<PostResponse> findAll();
    
    PostResponse update(Long id, PostRequest request);
    
    void deleteById(Long id);
    
    boolean existsById(Long id);

}

