package edu.hunre.course_management.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name = "image_course")
public class ImageCourseEntity extends AbstractEntity{
    private String name;
    private String type;
    @Lob
    @Column(name = "file", columnDefinition = "LONGBLOB")
    private byte[] file;
    @Column(name = "path")
    private String uploadDir;

    @ManyToOne
    @JoinColumn(name = "course_id")
    private CourseEntity courseEntity;
}
