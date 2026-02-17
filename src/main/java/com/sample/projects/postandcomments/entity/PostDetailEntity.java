package com.sample.projects.postandcomments.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
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

    @NotBlank(message = "Description is required")
    @Size(min=1, max=5000, message="Description must be between 1 and 5000 characters")
    @Column(nullable = false, length = 5000)
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

