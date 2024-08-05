package edu.hunre.course_management.model.dto;

import lombok.Data;

import java.util.List;
@Data
public class CommentDTO {
    private Long id;
    private String content;
    private Long customerId;
    private Long courseId;
    private Long rateId;
}
