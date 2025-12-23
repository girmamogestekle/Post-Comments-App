package com.sample.projects.postandcomments.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

@Getter
@Setter
@ToString(exclude = {"postDetails", "comments", "tags"})
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity(name = "Post")
@Table(name = "post")
public class Post extends BaseEntity {

    @NotBlank(message = "Title is required")
    @Size(min = 1, max = 255, message = "Title must be between 1 and 255 characters")
    @Column(nullable = false, length = 255)
    private String title;

    @OneToOne(
            mappedBy = "post",
            cascade = CascadeType.ALL,
            orphanRemoval = true,
            fetch = FetchType.LAZY
    )
    private PostDetails postDetails;

    @OneToMany(
            mappedBy = "post",
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    @Builder.Default
    private List<PostComment> comments = new ArrayList<>();

    @ManyToMany(
            cascade = {
                    CascadeType.PERSIST,
                    CascadeType.MERGE
            }
    )
    @JoinTable(
            name = "post_tag",
            joinColumns = @JoinColumn(name = "post_id"),
            inverseJoinColumns = @JoinColumn(name = "tag_id")
    )
    @Builder.Default
    private Set<Tag> tags = new LinkedHashSet<>();

    public void addComment(PostComment comment) {
        if (comments == null) {
            comments = new ArrayList<>();
        }
        comments.add(comment);
        comment.setPost(this);
    }

    public void removeComment(PostComment comment) {
        if (comments != null) {
            comments.remove(comment);
            comment.setPost(null);
        }
    }

    // The addTag method is used for synchronizing the bidirectional association
    public void addTag(Tag tag) {
        if (tags == null) {
            tags = new LinkedHashSet<>();
        }
        tags.add(tag);
        if (tag.getPosts() == null) {
            tag.setPosts(new LinkedHashSet<>());
        }
        tag.getPosts().add(this);
    }

    // The removeTag method is used for synchronizing the bidirectional association
    public void removeTag(Tag tag) {
        if (tags != null) {
            tags.remove(tag);
        }
        if (tag.getPosts() != null) {
            tag.getPosts().remove(this);
        }
    }

    // The setDetails method is used for synchronizing both sides of this bidirectional association
    // And is used both for adding and removing the associated child entity.
    public void setDetails(PostDetails postDetails) {
        if(postDetails == null) {
            if(this.postDetails != null) this.postDetails.setPost(null);
        } else {
            postDetails.setPost(this);
        }
        this.postDetails = postDetails;
    }

    @Override
    public boolean equals(Object o) {
        if(this == o)  return true;
        if(!(o instanceof Post)) return false;
        return getId() != null && getId().equals(((Post) o).getId());
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }

}

