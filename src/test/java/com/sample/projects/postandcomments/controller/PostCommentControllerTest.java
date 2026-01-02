package com.sample.projects.postandcomments.controller;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.DisplayName;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;

@Slf4j
@WebMvcTest(controllers = PostCommentController.class)
@DisplayName("PostCommentController API Integration Tests")
public class PostCommentControllerTest {
}
