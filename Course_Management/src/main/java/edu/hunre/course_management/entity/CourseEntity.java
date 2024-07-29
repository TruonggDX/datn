package edu.hunre.course_management.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name = "cource")
public class CourseEntity extends AbstractEntity{
    private String name;
    private String code;
    private Long quantity;
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
    @JoinColumn(name = "account_id")
    @EqualsAndHashCode.Exclude
    private AccountEntity accountEntity;

    @ManyToOne
    @JoinColumn(name = "language_id")
    @EqualsAndHashCode.Exclude
    private LanguageEntity languageEntity;
}
