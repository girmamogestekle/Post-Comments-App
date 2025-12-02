package com.sample.projects.postandcomments.service;

import com.sample.projects.postandcomments.entity.PostComment;

import java.util.List;
import java.util.Optional;

public interface PostCommentService {
    
    PostComment save(PostComment comment);
    
    Optional<PostComment> findById(Long id);
    
    List<PostComment> findAll();
    
    List<PostComment> findByPostId(Long postId);
    
    PostComment update(Long id, PostComment comment);
    
    void deleteById(Long id);
    
    boolean existsById(Long id);

}

