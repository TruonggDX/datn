package edu.hunre.course_management.model.dto;
import lombok.Data;

import java.util.List;

@Data
public class CourseDTO {
        private Long id;
        private String name;
        private String code;
        private String description;
        private String shortDescription;
        private Double price;
        private Double discountPrice;
        private String requirements;
        private String categoryName;
        private Long categoryId;
        private Long parentCategoryId;
        private String languageName;
        private Long languageId;
        private Long accountId;
        private String benefit;
        private String accountName;
        private List<String> imageFile;
        private List<Long> imageId;
        private String createdBy;
        private String updateLast;
        private String parentCategoryName;
        private Long levelId;
        private String levelName;
        private Integer duration;
        private Integer numberContent;
}
