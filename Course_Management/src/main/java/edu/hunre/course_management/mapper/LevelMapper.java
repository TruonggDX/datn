package edu.hunre.course_management.mapper;

import edu.hunre.course_management.entity.LevelEntity;
import edu.hunre.course_management.model.dto.LevelDTO;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface LevelMapper {
    LevelDTO toDto(LevelEntity levelEntity);
    LevelEntity toEntity(LevelDTO levelDTO);
}
