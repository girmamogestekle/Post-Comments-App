package com.sample.projects.postandcomments.service.impl;

import com.sample.projects.postandcomments.entity.Tag;
import com.sample.projects.postandcomments.repository.TagRepository;
import com.sample.projects.postandcomments.service.TagService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class TagServiceImpl implements TagService {

    private final TagRepository tagRepository;

    @Autowired
    public TagServiceImpl(TagRepository tagRepository) {
        this.tagRepository = tagRepository;
    }

    @Override
    public Tag save(Tag tag) {
        // Check if tag with same name already exists
        Optional<Tag> existingTag = tagRepository.findAll().stream()
                .filter(t -> t.getName().equalsIgnoreCase(tag.getName()))
                .findFirst();
        
        if (existingTag.isPresent()) {
            return existingTag.get();
        }
        
        return tagRepository.save(tag);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Tag> findById(Long id) {
        return tagRepository.findById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Tag> findByName(String name) {
        return tagRepository.findAll().stream()
                .filter(tag -> tag.getName().equalsIgnoreCase(name))
                .findFirst();
    }

    @Override
    @Transactional(readOnly = true)
    public List<Tag> findAll() {
        return tagRepository.findAll();
    }

    @Override
    public Tag update(Long id, Tag tag) {
        return tagRepository.findById(id)
                .map(existingTag -> {
                    existingTag.setName(tag.getName());
                    return tagRepository.save(existingTag);
                })
                .orElseThrow(() -> new RuntimeException("Tag not found with id: " + id));
    }

    @Override
    public void deleteById(Long id) {
        if (!tagRepository.existsById(id)) {
            throw new RuntimeException("Tag not found with id: " + id);
        }
        tagRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public boolean existsById(Long id) {
        return tagRepository.existsById(id);
    }

}

