package com.sample.projects.postandcomments.mapper;

import com.sample.projects.postandcomments.dto.request.PostDetailRequest;
import com.sample.projects.postandcomments.dto.response.PostDetailResponse;
import com.sample.projects.postandcomments.entity.PostDetailEntity;
import com.sample.projects.postandcomments.entity.PostEntity;
import com.sample.projects.postandcomments.exception.ResourceNotFoundException;
import com.sample.projects.postandcomments.repository.PostRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;

@Slf4j
@Component
public class PostDetailMapper {

    private final PostRepository postRepository;

    public PostDetailMapper(PostRepository postRepository) {
        this.postRepository = postRepository;
    }

    public PostDetailEntity toPostDetailEntity(PostDetailRequest postDetailRequest){
        PostEntity postEntity = postRepository.findById(postDetailRequest.getPostId()).orElse(null);

        if(postEntity == null) {
            log.warn("Post Entity Not Found With Post Id: {}", postDetailRequest.getPostId());
            throw new ResourceNotFoundException("Post Entity Not Found With Post Id", postDetailRequest.getPostId());
        }

        return PostDetailEntity.builder()
                .postEntity(postEntity)
                .description(postDetailRequest.getDescription())
                .build();
    }


    public PostDetailResponse toPostDetailResponse(PostDetailEntity postDetailEntity){
        if(postDetailEntity == null) {
            return null;
        }

        PostDetailResponse.PostDetailResponseBuilder builder = PostDetailResponse.builder()
                .id(postDetailEntity.getId())
                .postId(postDetailEntity.getPostEntity() != null ? postDetailEntity.getPostEntity().getId() : null)
                .description(postDetailEntity.getDescription())
                .createdAt(postDetailEntity.getCreatedAt())
                .updatedAt(postDetailEntity.getUpdatedAt());

        return builder.build();
    }

    public List<PostDetailResponse> toPostDetailResponses(List<PostDetailEntity> postDetailEntities){
        if(postDetailEntities == null) {
            return null;
        }

        return postDetailEntities.stream()
                .map(this::toPostDetailResponse)
                .toList();
    }
}
