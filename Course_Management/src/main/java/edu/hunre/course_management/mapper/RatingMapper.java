package edu.hunre.course_management.mapper;

import edu.hunre.course_management.entity.RatingEntity;
import edu.hunre.course_management.model.dto.RatingDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface RatingMapper {
    @Mapping(source = "courseEntity.id", target = "customerId")
    @Mapping(source = "customerEntity.id", target = "courseId")
    RatingDTO toDto(RatingEntity ratingEntity);
    RatingEntity toEntity(RatingDTO ratingDTO);
}
