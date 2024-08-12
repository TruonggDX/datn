package edu.hunre.course_management.mapper;

import edu.hunre.course_management.entity.CartEntity;
import edu.hunre.course_management.entity.ImageCourseEntity;
import edu.hunre.course_management.model.dto.ImageCourseDTO;
import org.mapstruct.Mapper;

import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring")
public interface ImageSourseMapper {
    ImageCourseDTO toDto(ImageCourseEntity imageCourseEntity);
    ImageCourseEntity toEntity(ImageCourseDTO imageCourseDTO);


    default List<String> getImageFile(CartEntity cartEntity) {
        if (cartEntity.getCourseEntity() != null && cartEntity.getCourseEntity().getImageEntityList() != null) {
            return cartEntity.getCourseEntity().getImageEntityList().stream()
                    .map(imageCourse -> convertToBase64(imageCourse.getFile()))
                    .collect(Collectors.toList());
        }
        return new ArrayList<>();
    }

    default List<Long> getImageId(CartEntity cartEntity) {
        return cartEntity.getCourseEntity().getImageEntityList().stream()
                .map(ImageCourseEntity::getId)
                .collect(Collectors.toList());
    }

    default String convertToBase64(byte[] file) {
        return Base64.getEncoder().encodeToString(file);
    }
}
