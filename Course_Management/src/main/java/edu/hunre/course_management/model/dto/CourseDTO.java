package edu.hunre.course_management.model.dto;
import lombok.Data;

import java.util.List;

@Data
public class CourseDTO {
        private Long id;
        private String name;
        private String code;
        private String description;
        private String title;
        private Double price;
        private Integer duration;
        private Double discountPrice;
        private String requirements;
        private String categoryName;
        private Long categoryId;
        private String languageName;
        private Long languageId;
        private List<String> imageFile;
        private List<Long> imageId;
}
