package com.sample.projects.postandcomments;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class PostAndCommentsApplication {

    public static void main(String[] args) {
        SpringApplication.run(PostAndCommentsApplication.class, args);
    }

}
