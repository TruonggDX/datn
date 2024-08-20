package edu.hunre.course_management.service.impl;

import edu.hunre.course_management.entity.CartEntity;
import edu.hunre.course_management.entity.CourseEntity;
import edu.hunre.course_management.entity.CustomerEntity;
import edu.hunre.course_management.mapper.CartMapper;
import edu.hunre.course_management.model.dto.CartDTO;
import edu.hunre.course_management.model.request.CartFilterRequest;
import edu.hunre.course_management.model.response.BaseResponse;
import edu.hunre.course_management.repository.CartRepository;
import edu.hunre.course_management.repository.CourseRepository;
import edu.hunre.course_management.repository.CustomerRepository;
import edu.hunre.course_management.service.ICartService;
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
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ICartImpl implements ICartService {
    @Autowired
    private CartRepository cartRepository;
    @Autowired
    private CartMapper cartMapper;
    @Autowired
    private CustomerRepository customerRepository;
    @Autowired
    private CourseRepository courseRepository;

    @Override
    public BaseResponse<Page<CartDTO>> getCourseByCustomerId(int page, int size) {
        BaseResponse<Page<CartDTO>> response = new BaseResponse<>();
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentUsername = authentication.getName();
        CustomerEntity customerEntity = customerRepository.findByUsername(currentUsername);
        Pageable pageable = PageRequest.of(page, size);
        Page<CartEntity> cartEntities = cartRepository.getCourseOfCustomerInCart(customerEntity.getId(), pageable);
        List<CartDTO> cartDTOS = cartEntities.getContent()
                .stream()
                .map(cartMapper::toDto)
                .collect(Collectors.toList());
        Page<CartDTO> pageData = new PageImpl<>(cartDTOS, pageable, cartEntities.getTotalElements());
        response.setData(pageData);
        response.setMessage(Constant.HTTP_MESSAGE.SUCCESS);
        response.setCode(HttpStatus.OK.value());
        return response;
    }

    @Override
    public BaseResponse<Long> countCourse() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated()) {
            String currentUsername = authentication.getName();
            CustomerEntity currentUser = customerRepository.findByUsername(currentUsername);
            if (currentUser != null) {
                Long cartCount = cartRepository.countCourseByCustomerId(currentUser.getId());
                BaseResponse<Long> response = new BaseResponse<>();
                response.setData(cartCount);
                response.setMessage(Constant.HTTP_MESSAGE.SUCCESS);
                response.setCode(HttpStatus.OK.value());
                return response;
            } else {
                BaseResponse<Long> response = new BaseResponse<>();
                response.setMessage(Constant.HTTP_MESSAGE.FAILED);
                response.setCode(HttpStatus.NOT_FOUND.value());
                return response;
            }
        } else {
            BaseResponse<Long> response = new BaseResponse<>();
            response.setMessage(Constant.HTTP_MESSAGE.FAILED);
            response.setCode(HttpStatus.UNAUTHORIZED.value());
            return response;
        }
    }

    @Override
    public BaseResponse<?> deleteCourseInCart(Long id) {
        BaseResponse<CartDTO> response = new BaseResponse<>();
        Optional<CartEntity> cartEntity = cartRepository.findById(id);
        if (cartEntity.isEmpty()) {
            response.setMessage(Constant.HTTP_MESSAGE.FAILED);
            response.setCode(HttpStatus.NOT_FOUND.value());
            return response;
        }
        CartEntity cart = cartEntity.get();
        cart.setDeleted(true);
        cart.setModifiedDate(LocalDateTime.now());
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        cart.setModifiedBy(authentication.getName());
        cartRepository.save(cart);
        CartDTO cartDTO = cartMapper.toDto(cart);
        response.setData(cartDTO);
        response.setMessage(Constant.HTTP_MESSAGE.SUCCESS);
        response.setCode(HttpStatus.OK.value());
        return response;
    }

    @Override
    public BaseResponse<?> addCourseInCart(CartDTO cartDTO) {
        BaseResponse<CartDTO> response = new BaseResponse<>();
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            response.setCode(HttpStatus.UNAUTHORIZED.value());
            response.setMessage(Constant.HTTP_MESSAGE.FAILED);
            return response;
        }

        String currentUsername = authentication.getName();
        CustomerEntity currentUser = customerRepository.findByUsername(currentUsername);

        if (currentUser == null) {
            response.setCode(HttpStatus.UNAUTHORIZED.value());
            response.setMessage(Constant.HTTP_MESSAGE.FAILED);
            return response;
        }

        Optional<CourseEntity> courseEntity = courseRepository.findById(cartDTO.getCourseId());

        if (courseEntity.isEmpty()) {
            response.setMessage(Constant.HTTP_MESSAGE.FAILED);
            response.setCode(HttpStatus.NOT_FOUND.value());
            return response;
        }

        Optional<CartEntity> existingCartItem = cartRepository.findByCustomerEntityAndCourseEntity(currentUser,courseEntity.get());

        if (existingCartItem.isPresent()) {
            response.setMessage(Constant.HTTP_MESSAGE.FAILED);
            response.setCode(HttpStatus.CONFLICT.value());
            return response;
        }

        CartEntity cartEntity = new CartEntity();
        cartEntity.setId(cartDTO.getId());
        cartEntity.setQuantity(1L);
        cartEntity.setCustomerEntity(currentUser);
        cartEntity.setCourseEntity(courseEntity.get());
        cartEntity.setCreatedDate(LocalDateTime.now());
        cartEntity.setCreatedBy(currentUsername);
        cartEntity.setPrice(courseEntity.get().getPrice());
        cartEntity.setDeleted(false);

        cartRepository.save(cartEntity);

        response.setData(cartMapper.toDto(cartEntity));
        response.setMessage(Constant.HTTP_MESSAGE.SUCCESS);
        response.setCode(HttpStatus.OK.value());

        return response;
    }


    @Override
    public BaseResponse<?> findCourseById(Long id) {
        BaseResponse<CartDTO> response = new BaseResponse<>();
        Optional<CartEntity> cartEntity = cartRepository.findById(id);
        if (cartEntity.isEmpty()) {
            response.setMessage(Constant.HTTP_MESSAGE.FAILED);
            response.setCode(HttpStatus.NOT_FOUND.value());
            return response;
        }
        CartEntity cart = cartEntity.get();
        if (cart.getDeleted()) {
            response.setMessage(Constant.HTTP_MESSAGE.FAILED);
            response.setCode(HttpStatus.NOT_FOUND.value());
            return response;
        }
        response.setData(cartMapper.toDto(cart));
        response.setMessage(Constant.HTTP_MESSAGE.SUCCESS);
        response.setCode(HttpStatus.OK.value());
        return response;
    }
}
