package edu.hunre.course_management.mapper;

import edu.hunre.course_management.entity.CustomerEntity;
import edu.hunre.course_management.model.dto.CustomerDTO;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface CustomerMapper {
    CustomerDTO toDTO(CustomerEntity customerEntity);
    CustomerEntity toEntity (CustomerDTO customerDTO);
}
