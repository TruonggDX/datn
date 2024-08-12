package edu.hunre.course_management.mapper;

import edu.hunre.course_management.entity.OrderDetailEntity;
import edu.hunre.course_management.model.dto.OrderDetailDTO;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface OrderDetailMapper {
    OrderDetailDTO toDto(OrderDetailEntity orderDetailEntity);
    OrderDetailEntity toEntity(OrderDetailDTO orderDetailDTO);
}
