package edu.hunre.course_management.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@Entity
@Table(name = "order_detail")
public class OrderDetailEntity extends AbstractEntity{
    private Long quantity;
    private Double totalPrice;
    @ManyToOne
    @JoinColumn(name = "order_id")
    @ToString.Exclude
    private OrderEntity orderEntity;

    @ManyToOne
    @JoinColumn(name = "course_id")
    @ToString.Exclude
    private CourseEntity courseEntity;
}
