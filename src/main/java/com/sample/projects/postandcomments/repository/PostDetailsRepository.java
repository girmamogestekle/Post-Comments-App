package com.sample.projects.postandcomments.repository;

import com.sample.projects.postandcomments.entity.PostDetails;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PostDetailsRepository extends JpaRepository<PostDetails, Long> {
}

