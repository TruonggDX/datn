package edu.hunre.course_management.service;

import edu.hunre.course_management.model.dto.RatingDTO;
import edu.hunre.course_management.model.response.BaseResponse;
import org.springframework.data.domain.Page;

public interface IRatingService {
    BaseResponse<Page<RatingDTO>> getAll(RatingDTO ratingDTO, int page, int size);
    BaseResponse<?> addRating(RatingDTO ratingDTO);
    BaseResponse<?> updateRating(Long id,RatingDTO ratingDTO);
    BaseResponse<?> deleteRating(Long id);
    BaseResponse<?> findById(Long id);
}
