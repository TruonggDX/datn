package edu.hunre.course_management.entity;

import jakarta.persistence.*;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.Set;

@Getter
@Setter
@Entity
@Table(name = "orders")
public class OrderEntity extends AbstractEntity{
    private String code;
    @ManyToOne
    @JoinColumn(name = "customer_id")
    @ToString.Exclude
    private CustomerEntity customerEntity;


    @OneToMany(mappedBy = "orderEntity", cascade = CascadeType.ALL,fetch = FetchType.LAZY)
    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    private Set<OrderDetailEntity> orderDetailEntities;
}
