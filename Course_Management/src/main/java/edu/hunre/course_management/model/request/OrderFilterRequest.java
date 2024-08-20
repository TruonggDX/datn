package edu.hunre.course_management.model.request;

import lombok.Data;

@Data
public class OrderFilterRequest {
    private Long id;
    private String code;
    private Long customerId;
    private String customerName;
    private String courseName;
    private String courseId;
    private String description;
    private String languageName;
}
