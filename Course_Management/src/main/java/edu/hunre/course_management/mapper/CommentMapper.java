package edu.hunre.course_management.mapper;

import edu.hunre.course_management.entity.CommentEntity;
import edu.hunre.course_management.model.dto.CommentDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface CommentMapper {
    @Mapping(source = "courseEntity.id", target = "customerId")
    @Mapping(source = "customerEntity.id", target = "courseId")
    @Mapping(source = "ratingEntity.id", target = "rateId")
    CommentDTO toDto(CommentEntity commentEntity);
    CommentEntity toEntity(CommentDTO commentDTO);
}
