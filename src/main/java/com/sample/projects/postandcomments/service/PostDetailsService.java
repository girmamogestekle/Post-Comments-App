package com.sample.projects.postandcomments.service;

import com.sample.projects.postandcomments.dto.request.PostDetailRequest;
import com.sample.projects.postandcomments.dto.response.PostDetailResponse;
import com.sample.projects.postandcomments.entity.PostDetailEntity;

import java.util.List;
import java.util.Optional;

public interface PostDetailsService {

    PostDetailResponse save(PostDetailRequest postDetailRequest);
    
    Optional<PostDetailEntity> findById(Long id);
    
    List<PostDetailResponse> findAll();
    
    Optional<PostDetailEntity> findByPostId(Long postId);
    
    PostDetailEntity update(Long id, PostDetailEntity postDetailsEntity);
    
    void deleteById(Long id);
    
    boolean existsById(Long id);

}

