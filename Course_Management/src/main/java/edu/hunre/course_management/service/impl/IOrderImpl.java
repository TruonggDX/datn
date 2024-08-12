package edu.hunre.course_management.service.impl;

import edu.hunre.course_management.entity.*;
import edu.hunre.course_management.mapper.OrderMapper;
import edu.hunre.course_management.model.dto.CourseDTO;
import edu.hunre.course_management.model.dto.OrderDTO;
import edu.hunre.course_management.model.request.OrderFilterRequest;
import edu.hunre.course_management.model.response.BaseResponse;
import edu.hunre.course_management.repository.CustomerRepository;
import edu.hunre.course_management.repository.OrderDetailRepository;
import edu.hunre.course_management.repository.OrderRepository;
import edu.hunre.course_management.service.IOrderService;
import edu.hunre.course_management.utils.Constant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class IOrderImpl implements IOrderService {
    @Autowired
    private OrderMapper orderMapper;
    @Autowired
    private OrderRepository orderRepository;
    @Autowired
    private CustomerRepository customerRepository;
    @Autowired
    private OrderDetailRepository orderDetailRepository;

    @Override
    public BaseResponse<Page<OrderDTO>> getAll(OrderFilterRequest filterRequest, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<OrderEntity> pages = orderRepository.findAllByFilter(filterRequest, pageable);


        List<OrderDTO> orderDTOList = new ArrayList<>();

        for (OrderEntity orderEntity : pages.getContent()) {
            OrderDTO orderDTO = orderMapper.toDto(orderEntity);
            List<OrderDetailEntity> orderDetails = orderDetailRepository.findByCode(orderEntity.getId());

            List<String> courseName = new ArrayList<>();
            for (OrderDetailEntity orderDetail : orderDetails) {
                CourseEntity courseEntity = orderDetail.getCourseEntity();
                courseName.add(courseEntity.getName());
            }
            orderDTO.setCourseName(String.join(", ", courseName));
            List<Long> orderDetailId = orderEntity.getOrderDetailEntities().stream().map(OrderDetailEntity::getId).collect(Collectors.toList());
            orderDTO.setOrderDetailsId(orderDetailId);
            orderDTOList.add(orderDTO);
        }

        Page<OrderDTO> pageData = new PageImpl<>(orderDTOList, pageable, pages.getTotalElements());
        BaseResponse<Page<OrderDTO>> response = new BaseResponse<>();
        response.setCode(HttpStatus.OK.value());
        response.setMessage(Constant.HTTP_MESSAGE.SUCCESS);
        response.setData(pageData);
        return response;
    }

    @Override
    public BaseResponse<?> createOrder(OrderDTO orderDTO) {
        BaseResponse<OrderDTO> response = new BaseResponse<>();
        Optional<CustomerEntity> customer = customerRepository.findById(orderDTO.getCustomerId());
        if (customer.isEmpty()) {
            response.setCode(HttpStatus.BAD_REQUEST.value());
            response.setMessage(Constant.HTTP_MESSAGE.FAILED);
            return response;
        }
        OrderEntity orderEntity = orderMapper.toEntity(orderDTO);
        orderEntity.setCustomerEntity(customer.get());
        orderEntity.setCreatedDate(LocalDateTime.now());
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        orderEntity.setCreatedBy(authentication.getName());
        orderEntity.setDeleted(false);
        orderRepository.save(orderEntity);
        orderDTO = orderMapper.toDto(orderEntity);
        response.setCode(HttpStatus.OK.value());
        response.setMessage(Constant.HTTP_MESSAGE.SUCCESS);
        response.setData(orderDTO);
        return response;
    }

    @Override
    public BaseResponse<?> findById(Long id) {
        BaseResponse<OrderDTO> response = new BaseResponse<>();
        Optional<OrderEntity> order = orderRepository.findById(id);
        if (order.isEmpty()) {
            response.setCode(HttpStatus.NOT_FOUND.value());
            response.setMessage(Constant.HTTP_MESSAGE.FAILED);
            return response;
        }
        OrderEntity orderEntity = order.get();
        if (orderEntity.getDeleted()) {
            response.setCode(HttpStatus.BAD_REQUEST.value());
            response.setMessage(Constant.HTTP_MESSAGE.FAILED);
            return response;
        }
        response.setCode(HttpStatus.OK.value());
        response.setMessage(Constant.HTTP_MESSAGE.SUCCESS);
        response.setData(orderMapper.toDto(orderEntity));
        return response;
    }

    @Override
    public BaseResponse<?> deleteOrder(Long id) {
        BaseResponse<OrderDTO> response = new BaseResponse<>();
        Optional<OrderEntity> order = orderRepository.findById(id);
        if (order.isEmpty()) {
            response.setCode(HttpStatus.NOT_FOUND.value());
            response.setMessage(Constant.HTTP_MESSAGE.FAILED);
            return response;
        }
        OrderEntity orderEntity = order.get();
        orderEntity.setModifiedDate(LocalDateTime.now());
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        orderEntity.setModifiedBy(authentication.getName());
        orderEntity.setDeleted(true);
        orderRepository.save(orderEntity);

        OrderDTO orderDTO = orderMapper.toDto(orderEntity);
        response.setCode(HttpStatus.OK.value());
        response.setMessage(Constant.HTTP_MESSAGE.SUCCESS);
        response.setData(orderDTO);
        return response;
    }
}
