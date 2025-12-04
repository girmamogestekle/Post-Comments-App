package com.sample.projects.postandcomments.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;

@Getter
@Setter
@ToString(exclude = "post")
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity(name = "PostDetails")
@Table(name = "post_details")
public class PostDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @MapsId
    private Post post;

    @Column(length = 5000)
    private String description;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof PostDetails)) return false;
        return id != null && id.equals(((PostDetails) o).getId());
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}

