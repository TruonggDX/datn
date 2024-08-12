package edu.hunre.course_management.service;

import edu.hunre.course_management.model.dto.CartDTO;
import edu.hunre.course_management.model.request.CartFilterRequest;
import edu.hunre.course_management.model.response.BaseResponse;
import org.springframework.data.domain.Page;

public interface ICartService {
    BaseResponse<Page<CartDTO>> getCourseByCustomerId(int page, int size);
    BaseResponse<Long> countCourse();
    BaseResponse<?> deleteCourseInCart(Long id);
    BaseResponse<?> addCourseInCart(CartDTO cartDTO);
    BaseResponse<?> findCourseById(Long id);
}
