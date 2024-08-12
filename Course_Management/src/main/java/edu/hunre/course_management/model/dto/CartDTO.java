package edu.hunre.course_management.model.dto;

import lombok.Data;

import java.util.List;

@Data
public class CartDTO {
    private Long id;
    private String courseName;
    private Long courseId;
    private Long customerId;
    private String customerName;
    private String createdBy;
    private Double price;
    private List<String> imageFile;
    private List<Long> imageId;
}
