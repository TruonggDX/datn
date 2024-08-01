package edu.hunre.course_management.model.dto;

import lombok.Data;

@Data
public class ImageCourseDTO {
    private Long id;
    private String name;
    private String type;
    private byte[] file;
    private String uploadDir;
}

