package com.sample.projects.postandcomments.controller;

import com.sample.projects.postandcomments.entity.TagEntity;
import com.sample.projects.postandcomments.service.TagService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/tags")
public class TagController {

    private final TagService tagService;

    @Autowired
    public TagController(TagService tagService) {
        this.tagService = tagService;
    }

    @PostMapping
    public ResponseEntity<TagEntity> createTag(@RequestBody TagEntity tagEntity) {
        TagEntity createdTagEntity = tagService.save(tagEntity);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdTagEntity);
    }

    @GetMapping("/{id}")
    public ResponseEntity<TagEntity> getTagById(@PathVariable Long id) {
        return tagService.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/name/{name}")
    public ResponseEntity<TagEntity> getTagByName(@PathVariable String name) {
        return tagService.findByName(name)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping
    public ResponseEntity<List<TagEntity>> getAllTags() {
        List<TagEntity> tagEntities = tagService.findAll();
        return ResponseEntity.ok(tagEntities);
    }

    @PutMapping("/{id}")
    public ResponseEntity<TagEntity> updateTag(@PathVariable Long id, @RequestBody TagEntity tagEntity) {
        if (!tagService.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        TagEntity updatedTagEntity = tagService.update(id, tagEntity);
        return ResponseEntity.ok(updatedTagEntity);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTag(@PathVariable Long id) {
        if (!tagService.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        tagService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}

