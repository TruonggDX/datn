package edu.hunre.course_management.mapper;

import edu.hunre.course_management.entity.OrderEntity;
import edu.hunre.course_management.model.dto.OrderDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface OrderMapper {
    @Mapping(source = "customerEntity.id", target = "customerId")
    @Mapping(source = "customerEntity.fullname", target = "customerName")
    OrderDTO toDto(OrderEntity orderEntity);
    OrderEntity toEntity(OrderDTO orderDTO);
}
