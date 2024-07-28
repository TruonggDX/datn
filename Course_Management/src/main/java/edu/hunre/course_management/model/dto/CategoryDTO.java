package edu.hunre.course_management.model.dto;

import lombok.Data;

@Data
public class CategoryDTO {
    private Long id;
    private String name;
    private Long parentId;
    private String parentName;
}
