package edu.hunre.course_management.entity;

import jakarta.persistence.*;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

@Entity
@Getter
@Setter
@Table(name = "course")
public class CourseEntity extends AbstractEntity {
    private String name;
    private String code;
    private String description;
    private String shortDescription;
    private Double price;
    private Double discountPrice;
    private String requirements;
    private String benefit;
    @Column(name = "wishlist", columnDefinition = "tinyint(1) default 0")
    private Boolean wishlist;

    @ManyToOne
    @JoinColumn(name = "category_id")
    @EqualsAndHashCode.Exclude
    private CategoryEntity categoryEntity;


    @ManyToOne
    @JoinColumn(name = "language_id")
    @EqualsAndHashCode.Exclude
    private LanguageEntity languageEntity;

    @ManyToOne
    @JoinColumn(name = "account_id")
    @EqualsAndHashCode.Exclude
    private AccountEntity accountEntity;

    @ManyToOne
    @JoinColumn(name = "level_id")
    @EqualsAndHashCode.Exclude
    private LevelEntity levelEntity;



    @OneToMany(mappedBy = "courseEntity", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    private List<ContentCourseEntity> contentCourseEntities;

    @OneToMany(mappedBy = "courseEntity", cascade = CascadeType.ALL)
    private List<ImageCourseEntity> imageEntityList;
}
