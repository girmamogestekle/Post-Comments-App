package com.sample.projects.postandcomments.repository;

import com.sample.projects.postandcomments.entity.PostDetailEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PostDetailRepository extends JpaRepository<PostDetailEntity, Long> {
}

