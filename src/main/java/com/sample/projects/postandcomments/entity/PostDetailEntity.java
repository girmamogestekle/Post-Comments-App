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
@Entity(name = "PostDetailEntity")
@Table(name = "post_detail")
public class PostDetailEntity extends BaseEntity {

    @OneToOne(fetch = FetchType.LAZY)
    @MapsId
    @JoinColumn(name = "id")
    private PostEntity postEntity;

    @Column(length = 5000)
    private String description;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof PostDetailEntity)) return false;
        return getId() != null && getId().equals(((PostDetailEntity) o).getId());
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}

