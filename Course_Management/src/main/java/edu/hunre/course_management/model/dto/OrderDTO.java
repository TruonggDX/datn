package edu.hunre.course_management.model.dto;

import lombok.Data;

import java.util.List;
import java.util.Set;

@Data
public class OrderDTO {
    private Long id;
    private String code;
    private Long customerId;
    private Set<OrderDetailDTO> orderDetails;
}
