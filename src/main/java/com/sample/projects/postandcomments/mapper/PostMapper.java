package com.sample.projects.postandcomments.mapper;

import com.sample.projects.postandcomments.dto.request.PostRequest;
import com.sample.projects.postandcomments.dto.response.PostCommentResponse;
import com.sample.projects.postandcomments.dto.response.PostDetailsResponse;
import com.sample.projects.postandcomments.dto.response.PostResponse;
import com.sample.projects.postandcomments.dto.response.TagResponse;
import com.sample.projects.postandcomments.entity.Post;
import com.sample.projects.postandcomments.entity.PostComment;
import com.sample.projects.postandcomments.entity.PostDetails;
import com.sample.projects.postandcomments.entity.Tag;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class PostMapper {

    public Post toEntity(PostRequest request) {
        return Post.builder()
                .title(request.getTitle())
                .build();
    }

    public PostResponse toResponse(Post post) {
        if (post == null) {
            return null;
        }

        PostResponse.PostResponseBuilder builder = PostResponse.builder()
                .id(post.getId())
                .title(post.getTitle())
                .createdAt(post.getCreatedAt())
                .updatedAt(post.getUpdatedAt());

        // Map post details
        if (post.getPostDetails() != null) {
            builder.postDetails(toPostDetailsResponse(post.getPostDetails()));
        }

        // Map comments
        if (post.getComments() != null && !post.getComments().isEmpty()) {
            builder.comments(post.getComments().stream()
                    .map(this::toPostCommentResponse)
                    .collect(Collectors.toList()));
        }

        // Map tags
        if (post.getTags() != null && !post.getTags().isEmpty()) {
            builder.tags(post.getTags().stream()
                    .map(this::toTagResponse)
                    .collect(Collectors.toSet()));
        }

        return builder.build();
    }

    public PostDetailsResponse toPostDetailsResponse(PostDetails postDetails) {
        if (postDetails == null) {
            return null;
        }

        return PostDetailsResponse.builder()
                .id(postDetails.getId())
                .postId(postDetails.getPost() != null ? postDetails.getPost().getId() : null)
                .description(postDetails.getDescription())
                .createdAt(postDetails.getCreatedAt())
                .updatedAt(postDetails.getUpdatedAt())
                .build();
    }

    public PostCommentResponse toPostCommentResponse(PostComment comment) {
        if (comment == null) {
            return null;
        }

        return PostCommentResponse.builder()
                .id(comment.getId())
                .review(comment.getReview())
                .postId(comment.getPost() != null ? comment.getPost().getId() : null)
                .createdAt(comment.getCreatedAt())
                .updatedAt(comment.getUpdatedAt())
                .build();
    }

    public TagResponse toTagResponse(Tag tag) {
        if (tag == null) {
            return null;
        }

        return TagResponse.builder()
                .id(tag.getId())
                .name(tag.getName())
                .build();
    }

    public List<PostResponse> toResponseList(List<Post> posts) {
        if (posts == null) {
            return List.of();
        }

        return posts.stream()
                .map(this::toResponse)
                .toList();
    }
}

