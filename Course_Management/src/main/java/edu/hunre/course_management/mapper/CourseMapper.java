package edu.hunre.course_management.mapper;

import edu.hunre.course_management.entity.CourseEntity;
import edu.hunre.course_management.mapper.decorator.CourseMapperDecorator;
import edu.hunre.course_management.model.dto.CourseDTO;
import org.mapstruct.DecoratedWith;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
@DecoratedWith(CourseMapperDecorator.class)
public interface CourseMapper {
    @Mapping(source = "categoryEntity.name", target = "categoryName")
    @Mapping(source = "categoryEntity.id", target = "categoryId")
    @Mapping(source = "languageEntity.name", target = "languageName")
    @Mapping(source = "languageEntity.id", target = "languageId")

    CourseDTO toDTO(CourseEntity courseEntity);
    CourseEntity toEntity(CourseDTO courseDTO);
}
