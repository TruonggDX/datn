package edu.hunre.course_management.entity;

import jakarta.persistence.*;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Entity
@Getter
@Setter
@Table(name = "course")
public class CourseEntity extends AbstractEntity{
    private String name;
    private String code;
    private String description;
    private String title;
    private Double price;
    private Integer duration;
    private Double discountPrice;
    private String requirements;

    @ManyToOne
    @JoinColumn(name = "category_id")
    @EqualsAndHashCode.Exclude
    private CategoryEntity categoryEntity;


    @ManyToOne
    @JoinColumn(name = "language_id")
    @EqualsAndHashCode.Exclude
    private LanguageEntity languageEntity;

    @OneToMany(mappedBy = "courseEntity", cascade = CascadeType.ALL)
    private List<ImageCourseEntity> imageEntityList;
}
