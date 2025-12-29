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
@ToString(exclude = {"postDetailsEntity", "comments", "tagEntities"})
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@Entity(name = "PostEntity")
@Table(name = "post")
public class PostEntity extends BaseEntity {

    @NotBlank(message = "Title is required")
    @Size(min = 1, max = 255, message = "Title must be between 1 and 255 characters")
    @Column(nullable = false, length = 255)
    private String title;

    @OneToOne(
            mappedBy = "postEntity",
            cascade = CascadeType.ALL,
            orphanRemoval = true,
            fetch = FetchType.LAZY
    )
    private PostDetailEntity postDetailEntity;

    @OneToMany(
            mappedBy = "postEntity",
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    @Builder.Default
    private List<PostCommentsEntity> comments = new ArrayList<>();

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
    private Set<TagEntity> tagEntities = new LinkedHashSet<>();

    public void addComment(PostCommentsEntity comment) {
        if (comments == null) {
            comments = new ArrayList<>();
        }
        comments.add(comment);
        comment.setPostEntity(this);
    }

    public void removeComment(PostCommentsEntity comment) {
        if (comments != null) {
            comments.remove(comment);
            comment.setPostEntity(null);
        }
    }

    // The addTag method is used for synchronizing the bidirectional association
    public void addTag(TagEntity tagEntity) {
        if (tagEntities == null) {
            tagEntities = new LinkedHashSet<>();
        }
        tagEntities.add(tagEntity);
        if (tagEntity.getPostEntities() == null) {
            tagEntity.setPostEntities(new LinkedHashSet<>());
        }
        tagEntity.getPostEntities().add(this);
    }

    // The removeTag method is used for synchronizing the bidirectional association
    public void removeTag(TagEntity tagEntity) {
        if (tagEntities != null) {
            tagEntities.remove(tagEntity);
        }
        if (tagEntity.getPostEntities() != null) {
            tagEntity.getPostEntities().remove(this);
        }
    }

    // The setDetails method is used for synchronizing both sides of this bidirectional association
    // And is used both for adding and removing the associated child entity.
    public void setDetails(PostDetailEntity postDetailsEntity) {
        if(postDetailsEntity == null) {
            if(this.postDetailEntity != null) this.postDetailEntity.setPostEntity(null);
        } else {
            postDetailsEntity.setPostEntity(this);
        }
        this.postDetailEntity = postDetailsEntity;
    }

    @Override
    public boolean equals(Object o) {
        if(this == o)  return true;
        if(!(o instanceof PostEntity)) return false;
        return getId() != null && getId().equals(((PostEntity) o).getId());
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }

}

