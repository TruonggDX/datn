package edu.hunre.course_management.model.request;

import lombok.Data;

import java.util.List;

@Data
public class CourseFilterRequest {
    private String name;
    private String code;
    private Double price;
    private Long categoryId;
    private Long languageId;
    private List<Long> imageId;
}
