package com.sample.projects.postandcomments.service.impl;

import com.sample.projects.postandcomments.entity.PostCommentsEntity;
import com.sample.projects.postandcomments.repository.PostCommentRepository;
import com.sample.projects.postandcomments.service.PostCommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class PostCommentServiceImpl implements PostCommentService {

    private final PostCommentRepository postCommentRepository;

    @Autowired
    public PostCommentServiceImpl(PostCommentRepository postCommentRepository) {
        this.postCommentRepository = postCommentRepository;
    }

    @Override
    public PostCommentsEntity save(PostCommentsEntity comment) {
        if (comment.getCreatedAt() == null) {
            comment.setCreatedAt(LocalDateTime.now());
        }
        comment.setUpdatedAt(LocalDateTime.now());
        return postCommentRepository.save(comment);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<PostCommentsEntity> findById(Long id) {
        return postCommentRepository.findById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public List<PostCommentsEntity> findAll() {
        return postCommentRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public List<PostCommentsEntity> findByPostId(Long postId) {
        return postCommentRepository.findAll().stream()
                .filter(comment -> comment.getPostEntity() != null && comment.getPostEntity().getId().equals(postId))
                .toList();
    }

    @Override
    public PostCommentsEntity update(Long id, PostCommentsEntity comment) {
        return postCommentRepository.findById(id)
                .map(existingComment -> {
                    existingComment.setComment(comment.getComment());
                    existingComment.setUpdatedAt(LocalDateTime.now());
                    return postCommentRepository.save(existingComment);
                })
                .orElseThrow(() -> new RuntimeException("PostCommentsEntity not found with id: " + id));
    }

    @Override
    public void deleteById(Long id) {
        if (!postCommentRepository.existsById(id)) {
            throw new RuntimeException("PostCommentsEntity not found with id: " + id);
        }
        postCommentRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public boolean existsById(Long id) {
        return postCommentRepository.existsById(id);
    }

}

