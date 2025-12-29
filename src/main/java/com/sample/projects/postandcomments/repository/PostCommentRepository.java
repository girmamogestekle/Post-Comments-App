package com.sample.projects.postandcomments.repository;

import com.sample.projects.postandcomments.entity.PostCommentsEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PostCommentRepository extends JpaRepository<PostCommentsEntity, Long> {
}

