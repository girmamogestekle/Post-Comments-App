package com.sample.projects.postandcomments.service;

import com.sample.projects.postandcomments.entity.Tag;

import java.util.List;
import java.util.Optional;

public interface TagService {
    
    Tag save(Tag tag);
    
    Optional<Tag> findById(Long id);
    
    Optional<Tag> findByName(String name);
    
    List<Tag> findAll();
    
    Tag update(Long id, Tag tag);
    
    void deleteById(Long id);
    
    boolean existsById(Long id);

}

