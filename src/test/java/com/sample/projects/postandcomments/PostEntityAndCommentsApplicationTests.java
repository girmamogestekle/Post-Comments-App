package com.sample.projects.postandcomments;

import com.sample.projects.postandcomments.config.TestAiConfig;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest
@ActiveProfiles("test")
@Import(TestAiConfig.class)
class PostEntityAndCommentsApplicationTests {

    @Test
    void contextLoads() {
    }

}
