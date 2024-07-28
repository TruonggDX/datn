package edu.hunre.course_management.entity;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
@Table(name = "category")
public class CategoryEntity extends AbstractEntity{
    private String name;
    @ManyToOne
    @JoinColumn(name = "parent_id")
    private CategoryEntity parent;
}
