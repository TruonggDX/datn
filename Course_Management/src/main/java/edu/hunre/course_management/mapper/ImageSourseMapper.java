package edu.hunre.course_management.mapper;

import edu.hunre.course_management.entity.ImageCourseEntity;
import edu.hunre.course_management.model.dto.ImageCourseDTO;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ImageSourseMapper {
    ImageCourseDTO toDto(ImageCourseEntity imageCourseEntity);
    ImageCourseEntity toEntity(ImageCourseDTO imageCourseDTO);
}
