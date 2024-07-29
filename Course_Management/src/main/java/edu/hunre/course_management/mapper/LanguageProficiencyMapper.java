package edu.hunre.course_management.mapper;

import edu.hunre.course_management.entity.LanguageEntity;
import edu.hunre.course_management.model.dto.LanguageDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface LanguageProficiencyMapper {
    LanguageDTO toDto(LanguageEntity languageEntity);
    LanguageEntity toEntity(LanguageDTO languageProficiencyDTO);
}
