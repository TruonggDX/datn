package edu.hunre.course_management.model.dto;

import lombok.Data;

@Data
public class ContentCourseDTO {
    private Long id;
    private String name;
    private String content;
    private String link;
    private Integer duration;
    private Integer number;
}
