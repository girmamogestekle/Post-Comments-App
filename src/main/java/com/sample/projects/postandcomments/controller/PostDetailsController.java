package com.sample.projects.postandcomments.controller;

import com.sample.projects.postandcomments.entity.PostDetails;
import com.sample.projects.postandcomments.service.PostDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/post-details")
public class PostDetailsController {

    private final PostDetailsService postDetailsService;

    @Autowired
    public PostDetailsController(PostDetailsService postDetailsService) {
        this.postDetailsService = postDetailsService;
    }

    @PostMapping
    public ResponseEntity<PostDetails> createPostDetails(@RequestBody PostDetails postDetails) {
        PostDetails createdDetails = postDetailsService.save(postDetails);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdDetails);
    }

    @GetMapping("/{id}")
    public ResponseEntity<PostDetails> getPostDetailsById(@PathVariable Long id) {
        return postDetailsService.findById(id)
                .map(details -> ResponseEntity.ok(details))
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping
    public ResponseEntity<List<PostDetails>> getAllPostDetails() {
        List<PostDetails> allDetails = postDetailsService.findAll();
        return ResponseEntity.ok(allDetails);
    }

    @GetMapping("/post/{postId}")
    public ResponseEntity<PostDetails> getPostDetailsByPostId(@PathVariable Long postId) {
        return postDetailsService.findByPostId(postId)
                .map(details -> ResponseEntity.ok(details))
                .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}")
    public ResponseEntity<PostDetails> updatePostDetails(@PathVariable Long id, @RequestBody PostDetails postDetails) {
        if (!postDetailsService.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        PostDetails updatedDetails = postDetailsService.update(id, postDetails);
        return ResponseEntity.ok(updatedDetails);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePostDetails(@PathVariable Long id) {
        if (!postDetailsService.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        postDetailsService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}

