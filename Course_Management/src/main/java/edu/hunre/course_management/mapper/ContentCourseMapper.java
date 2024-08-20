package edu.hunre.course_management.mapper;

import edu.hunre.course_management.entity.ContentCourseEntity;
import edu.hunre.course_management.model.dto.ContentCourseDTO;
import edu.hunre.course_management.model.request.ContentCourseRequest;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ContentCourseMapper {
    ContentCourseDTO toDto(ContentCourseEntity contentCourseEntity);
    ContentCourseEntity toEntity(ContentCourseRequest contentCourseDTO);

    ContentCourseRequest toDtoRequest(ContentCourseEntity contentCourseEntity);
    ContentCourseEntity toEntityRequest(ContentCourseRequest contentCourseRequest);
}
