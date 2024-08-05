package edu.hunre.course_management.model.dto;

import lombok.Data;

@Data
public class RatingDTO {
    private Long id;
    private Integer star;
    private Long customerId;
    private Long courseId;
}
