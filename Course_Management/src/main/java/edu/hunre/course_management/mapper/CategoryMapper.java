package edu.hunre.course_management.mapper;

import edu.hunre.course_management.entity.CategoryEntity;
import edu.hunre.course_management.model.dto.CategoryDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface CategoryMapper {
    @Mapping(source = "parent.id", target = "parentId")
    CategoryDTO toDTO(CategoryEntity categoryEntity);

    @Mapping(target = "parent", ignore = true)
    CategoryEntity toEntity(CategoryDTO categoryDTO);
}
