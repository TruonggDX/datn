package edu.hunre.course_management.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LevelCourseCountDTO {
    private Long levelId;
    private Long courseCount;
}
