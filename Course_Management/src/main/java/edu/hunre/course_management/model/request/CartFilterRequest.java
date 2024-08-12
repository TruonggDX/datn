package edu.hunre.course_management.model.request;

import lombok.Data;

@Data
public class CartFilterRequest {
    private Long id;
    private Long courseId;
    private Long customerId;
    private Double price;
    private Long quantity;
}
