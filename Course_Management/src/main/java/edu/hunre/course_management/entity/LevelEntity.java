package edu.hunre.course_management.entity;

import jakarta.persistence.*;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

@Entity
@Setter
@Getter
@Table(name = "level")
public class LevelEntity extends AbstractEntity{
    private String name;
    private String description;
    @OneToMany(mappedBy = "levelEntity", cascade = CascadeType.ALL,fetch = FetchType.LAZY)
    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    private List<CourseEntity> courseEntities;
}
