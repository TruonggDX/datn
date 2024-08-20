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
@Table(name = "content_course")
public class ContentCourseEntity extends AbstractEntity{
    private String name;
    private String content;
    private String link;
    private Integer duration;
    private Integer number;

    @ManyToOne
    @JoinColumn(name = "course_id")
    @EqualsAndHashCode.Exclude
    private CourseEntity courseEntity;

}
