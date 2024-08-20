package edu.hunre.course_management.mapper;

import edu.hunre.course_management.entity.ContentCourseEntity;
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
    @Mapping(source = "categoryEntity.parent.id", target = "parentCategoryId")
    @Mapping(source = "categoryEntity.parent.name", target = "parentCategoryName")
    @Mapping(source = "languageEntity.name", target = "languageName")
    @Mapping(source = "languageEntity.id", target = "languageId")
    @Mapping(source = "accountEntity.id", target = "accountId")
    @Mapping(source = "accountEntity.fullname", target = "accountName")
    @Mapping(source = "createdBy", target = "createdBy")
    @Mapping(source = "modifiedDate", target = "updateLast")
    @Mapping(source = "levelEntity.id", target = "levelId")
    @Mapping(source = "levelEntity.name", target = "levelName")
    CourseDTO toDTO(CourseEntity courseEntity);

    CourseEntity toEntity(CourseDTO courseDTO);



    default CourseDTO toDtoCustom(CourseEntity courseEntity) {
        CourseDTO courseDTO = toDTO(courseEntity);

        if (courseEntity.getContentCourseEntities() == null) {
            courseDTO.setDuration(0);
            courseDTO.setNumberContent(0);
        } else {
            Integer duration = 0;
            for (ContentCourseEntity detailCourseEntity : courseEntity.getContentCourseEntities()) {
                duration += detailCourseEntity.getDuration();
            }
            courseDTO.setNumberContent(courseEntity.getContentCourseEntities().size());
            courseDTO.setDuration(duration);
        }

        return courseDTO;
    }
}
