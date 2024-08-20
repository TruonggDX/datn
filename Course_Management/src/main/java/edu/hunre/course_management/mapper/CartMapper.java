package edu.hunre.course_management.mapper;

import edu.hunre.course_management.entity.CartEntity;
import edu.hunre.course_management.mapper.decorator.CartMapperDecorator;
import edu.hunre.course_management.model.dto.CartDTO;
import org.mapstruct.DecoratedWith;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;


@Mapper(componentModel = "spring")
@DecoratedWith(CartMapperDecorator.class)
public interface CartMapper {
    @Mapping(source = "customerEntity.fullname", target = "customerName")
    @Mapping(source = "customerEntity.id", target = "customerId")
    @Mapping(source = "courseEntity.name", target = "courseName")
    @Mapping(source = "courseEntity.id", target = "courseId")
    @Mapping(source = "courseEntity.price", target = "price")
    @Mapping(source = "courseEntity.createdBy", target = "createdBy")
    @Mapping(source = "courseEntity.levelEntity.name", target = "levelName")
    @Mapping(source = "courseEntity.levelEntity.id", target = "levelId")
    @Mapping(source = "courseEntity.languageEntity.name", target = "languageName")
    @Mapping(source = "courseEntity.languageEntity.id", target = "languageId")
    CartDTO toDto(CartEntity cartEntity);
    CartEntity toEntity(CartDTO cartDTO);


}
