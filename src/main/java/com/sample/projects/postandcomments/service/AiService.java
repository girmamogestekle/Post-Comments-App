package com.sample.projects.postandcomments.service;

import com.sample.projects.postandcomments.dto.response.AiResponse;
import com.sample.projects.postandcomments.dto.response.PostResponse;

public interface AiService {

    AiResponse explainPost(PostResponse postResponse);

}
