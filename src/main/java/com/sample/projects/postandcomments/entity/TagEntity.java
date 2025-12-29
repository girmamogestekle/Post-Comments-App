package com.sample.projects.postandcomments.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.NaturalId;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Getter
@Setter
@ToString(exclude = "postEntities")
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@Entity(name = "Tag")
@Table(name = "tag")
public class TagEntity extends BaseEntity {

    @Column(nullable = false, unique = true)
    @NaturalId
    private String name;

    @ManyToMany(
            mappedBy = "tagEntities",
            fetch = FetchType.LAZY
    )
    @Builder.Default
    private Set<PostEntity> postEntities = new HashSet<>();

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof TagEntity tagEntity)) return false;
        return Objects.equals(name, tagEntity.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name);
    }
}

