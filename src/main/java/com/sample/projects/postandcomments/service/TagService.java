package com.sample.projects.postandcomments.service;

import com.sample.projects.postandcomments.entity.TagEntity;

import java.util.List;
import java.util.Optional;

public interface TagService {
    
    TagEntity save(TagEntity tagEntity);
    
    Optional<TagEntity> findById(Long id);
    
    Optional<TagEntity> findByName(String name);
    
    List<TagEntity> findAll();
    
    TagEntity update(Long id, TagEntity tagEntity);
    
    void deleteById(Long id);
    
    boolean existsById(Long id);

}

