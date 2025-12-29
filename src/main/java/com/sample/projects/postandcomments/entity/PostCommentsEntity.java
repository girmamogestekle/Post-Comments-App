package com.sample.projects.postandcomments.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@ToString(exclude = "postEntity")
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@Entity(name = "PostCommentEntity")
@Table(name = "post_comments")
public class PostCommentsEntity extends BaseEntity {

    private String comment;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id")
    private PostEntity postEntity;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof PostCommentsEntity)) return false;
        return getId() != null && getId().equals(((PostCommentsEntity) o).getId());
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}

