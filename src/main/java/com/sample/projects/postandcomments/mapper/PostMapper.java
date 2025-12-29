package com.sample.projects.postandcomments.mapper;

import com.sample.projects.postandcomments.dto.request.PostRequest;
import com.sample.projects.postandcomments.dto.response.PostCommentResponse;
import com.sample.projects.postandcomments.dto.response.PostResponse;
import com.sample.projects.postandcomments.dto.response.TagResponse;
import com.sample.projects.postandcomments.entity.PostCommentsEntity;
import com.sample.projects.postandcomments.entity.PostEntity;
import com.sample.projects.postandcomments.entity.TagEntity;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class PostMapper {

    private final PostDetailMapper postDetailMapper;

    public PostMapper(PostDetailMapper postDetailMapper){
        this.postDetailMapper = postDetailMapper;
    }

    public PostEntity toEntity(PostRequest request) {
        return PostEntity.builder()
                .title(request.getTitle())
                .build();
    }

    public PostResponse toPostResponse(PostEntity postEntity) {
        if (postEntity == null) {
            return null;
        }

        PostResponse.PostResponseBuilder builder = PostResponse.builder()
                .id(postEntity.getId())
                .title(postEntity.getTitle())
                .createdAt(postEntity.getCreatedAt())
                .updatedAt(postEntity.getUpdatedAt());

        // Map postEntity details
        if (postEntity.getPostDetailEntity() != null) {
            builder.postDetailResponse(postDetailMapper.toPostDetailResponse(postEntity.getPostDetailEntity()));
        }

        // Map comments
        if (postEntity.getComments() != null && !postEntity.getComments().isEmpty()) {
            builder.comments(postEntity.getComments().stream()
                    .map(this::toPostCommentResponse)
                    .collect(Collectors.toList()));
        }

        // Map tagEntities
        if (postEntity.getTagEntities() != null && !postEntity.getTagEntities().isEmpty()) {
            builder.tags(postEntity.getTagEntities().stream()
                    .map(this::toTagResponse)
                    .collect(Collectors.toSet()));
        }

        return builder.build();
    }

    public PostCommentResponse toPostCommentResponse(PostCommentsEntity comment) {
        if (comment == null) {
            return null;
        }

        return PostCommentResponse.builder()
                .id(comment.getId())
                .review(comment.getComment())
                .postId(comment.getPostEntity() != null ? comment.getPostEntity().getId() : null)
                .createdAt(comment.getCreatedAt())
                .updatedAt(comment.getUpdatedAt())
                .build();
    }

    public TagResponse toTagResponse(TagEntity tagEntity) {
        if (tagEntity == null) {
            return null;
        }

        return TagResponse.builder()
                .id(tagEntity.getId())
                .name(tagEntity.getName())
                .build();
    }

    public List<PostResponse> toResponseList(List<PostEntity> postEntities) {
        if (postEntities == null) {
            return List.of();
        }

        return postEntities.stream()
                .map(this::toPostResponse)
                .toList();
    }
}

