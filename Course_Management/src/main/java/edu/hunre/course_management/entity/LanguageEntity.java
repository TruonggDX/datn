package edu.hunre.course_management.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name = "language")
public class LanguageEntity extends AbstractEntity{
    private String name;
    private String description;
}
