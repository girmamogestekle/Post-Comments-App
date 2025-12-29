package com.sample.projects.postandcomments.service;

import com.sample.projects.postandcomments.dto.request.PostDetailRequest;
import com.sample.projects.postandcomments.dto.response.PostDetailResponse;

import java.util.List;
import java.util.Optional;

public interface PostDetailsService {

    PostDetailResponse save(PostDetailRequest postDetailRequest);
    
    Optional<PostDetailResponse> findById(Long id);
    
    List<PostDetailResponse> findAll();
    
    Optional<PostDetailResponse> findByPostId(Long postId);

    PostDetailResponse update(Long id, PostDetailRequest postDetailRequest);
    
    void deleteById(Long id);
}

