package edu.hunre.course_management.model.dto;

import lombok.Data;

@Data
public class OrderDetailDTO {
    private Long id;
    private Long quantity;
    private Double totalPrice;
    private Long orderId;
    private Long courseId;
}
