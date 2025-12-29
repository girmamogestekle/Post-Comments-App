package com.sample.projects.postandcomments.service;

import com.sample.projects.postandcomments.entity.PostCommentsEntity;

import java.util.List;
import java.util.Optional;

public interface PostCommentService {
    
    PostCommentsEntity save(PostCommentsEntity comment);
    
    Optional<PostCommentsEntity> findById(Long id);
    
    List<PostCommentsEntity> findAll();
    
    List<PostCommentsEntity> findByPostId(Long postId);
    
    PostCommentsEntity update(Long id, PostCommentsEntity comment);
    
    void deleteById(Long id);
    
    boolean existsById(Long id);

}

