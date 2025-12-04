package com.sample.projects.postandcomments.controller;

import com.sample.projects.postandcomments.entity.PostComment;
import com.sample.projects.postandcomments.service.PostCommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/comments")
public class PostCommentController {

    private final PostCommentService postCommentService;

    @Autowired
    public PostCommentController(PostCommentService postCommentService) {
        this.postCommentService = postCommentService;
    }

    @PostMapping
    public ResponseEntity<PostComment> createComment(@RequestBody PostComment comment) {
        PostComment createdComment = postCommentService.save(comment);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdComment);
    }

    @GetMapping("/{id}")
    public ResponseEntity<PostComment> getCommentById(@PathVariable Long id) {
        return postCommentService.findById(id)
                .map(comment -> ResponseEntity.ok(comment))
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping
    public ResponseEntity<List<PostComment>> getAllComments() {
        List<PostComment> comments = postCommentService.findAll();
        return ResponseEntity.ok(comments);
    }

    @GetMapping("/post/{postId}")
    public ResponseEntity<List<PostComment>> getCommentsByPostId(@PathVariable Long postId) {
        List<PostComment> comments = postCommentService.findByPostId(postId);
        return ResponseEntity.ok(comments);
    }

    @PutMapping("/{id}")
    public ResponseEntity<PostComment> updateComment(@PathVariable Long id, @RequestBody PostComment comment) {
        if (!postCommentService.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        PostComment updatedComment = postCommentService.update(id, comment);
        return ResponseEntity.ok(updatedComment);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteComment(@PathVariable Long id) {
        if (!postCommentService.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        postCommentService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}

