package edu.hunre.course_management.service;

import edu.hunre.course_management.model.dto.OrderDTO;
import edu.hunre.course_management.model.request.OrderFilterRequest;
import edu.hunre.course_management.model.response.BaseResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface IOrderService {
    BaseResponse<Page<OrderDTO>> getAll(OrderFilterRequest filterRequest, int page,int size);
    BaseResponse<?> createOrder(OrderDTO orderDTO);
    BaseResponse<?> findById(Long id);
    BaseResponse<?> deleteOrder(Long id);
}
