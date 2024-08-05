package edu.hunre.course_management.service.impl;

import edu.hunre.course_management.entity.CommentEntity;
import edu.hunre.course_management.entity.CourseEntity;
import edu.hunre.course_management.entity.CustomerEntity;
import edu.hunre.course_management.entity.RatingEntity;
import edu.hunre.course_management.mapper.RatingMapper;
import edu.hunre.course_management.model.dto.RatingDTO;
import edu.hunre.course_management.model.response.BaseResponse;
import edu.hunre.course_management.repository.CommentRepository;
import edu.hunre.course_management.repository.CourseRepository;
import edu.hunre.course_management.repository.CustomerRepository;
import edu.hunre.course_management.repository.RatingRepository;
import edu.hunre.course_management.service.IRatingService;
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

@Service
public class IRatingImpl implements IRatingService {
    @Autowired
    private RatingMapper ratingMapper;
    @Autowired
    private RatingRepository ratingRepository;
    @Autowired
    private CustomerRepository customerRepository;
    @Autowired
    private CourseRepository courseRepository;
    @Override
    public BaseResponse<Page<RatingDTO>> getAll(RatingDTO ratingDTO, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<RatingEntity> pages = ratingRepository.findAllByFilter(ratingDTO, pageable);
        List<RatingDTO> ratingDTOS = pages.getContent().stream().map(ratingMapper::toDto).toList();
        BaseResponse<Page<RatingDTO>> response = new BaseResponse<>();
        response.setCode(HttpStatus.OK.value());
        response.setMessage(Constant.HTTP_MESSAGE.SUCCESS);
        response.setData(new PageImpl<>(ratingDTOS, pageable, pages.getTotalElements()));
        return response;

    }

    @Override
    public BaseResponse<?> addRating(RatingDTO ratingDTO) {
        BaseResponse<RatingDTO> response = new BaseResponse<>();
        Optional<CustomerEntity> customerEntity = customerRepository.findById(ratingDTO.getCustomerId());
        Optional<CourseEntity> courseEntity = courseRepository.findById(ratingDTO.getCourseId());
        if (customerEntity.isEmpty() || courseEntity.isEmpty()){
            response.setCode(HttpStatus.BAD_REQUEST.value());
            response.setMessage(Constant.HTTP_MESSAGE.FAILED);
            return response;
        }


        RatingEntity ratingEntity = ratingMapper.toEntity(ratingDTO);
        ratingEntity.setCustomerEntity(customerEntity.get());
        ratingEntity.setCourseEntity(courseEntity.get());
        ratingEntity.setCreatedDate(LocalDateTime.now());
        ratingEntity.setDeleted(false);
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        ratingEntity.setCreatedBy(auth.getName());
        ratingRepository.save(ratingEntity);
        ratingDTO.setId(ratingEntity.getId());
        ratingDTO = ratingMapper.toDto(ratingEntity);
        response.setCode(HttpStatus.OK.value());
        response.setMessage(Constant.HTTP_MESSAGE.SUCCESS);
        response.setData(ratingDTO);
        return response;
    }

    @Override
    public BaseResponse<?> updateRating(Long id, RatingDTO ratingDTO) {
        BaseResponse<RatingDTO> response = new BaseResponse<>();
        Optional<RatingEntity> ratingEntity = ratingRepository.findById(id);
        if (ratingEntity.isEmpty()){
            response.setCode(HttpStatus.BAD_REQUEST.value());
            response.setMessage(Constant.HTTP_MESSAGE.FAILED);
            return response;
        }
        Optional<CustomerEntity> customerEntity = customerRepository.findById(ratingDTO.getCustomerId());
        Optional<CourseEntity> courseEntity = courseRepository.findById(ratingDTO.getCourseId());
        if (customerEntity.isEmpty() || courseEntity.isEmpty()){
            response.setCode(HttpStatus.BAD_REQUEST.value());
            response.setMessage(Constant.HTTP_MESSAGE.FAILED);
            return response;
        }


        RatingEntity rating = ratingEntity.get();
        rating.setCustomerEntity(customerEntity.get());
        rating.setCourseEntity(courseEntity.get());

        rating.setModifiedDate(LocalDateTime.now());
        rating.setStar(ratingDTO.getStar());
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        rating.setModifiedBy(auth.getName());
        ratingRepository.save(rating);
        response.setCode(HttpStatus.OK.value());
        response.setMessage(Constant.HTTP_MESSAGE.SUCCESS);
        response.setData(ratingMapper.toDto(rating));

        return response;
    }

    @Override
    public BaseResponse<?> deleteRating(Long id) {
        BaseResponse<RatingDTO> response = new BaseResponse<>();
        Optional<RatingEntity> ratingEntity = ratingRepository.findById(id);
        if (ratingEntity.isEmpty()){
            response.setCode(HttpStatus.BAD_REQUEST.value());
            response.setMessage(Constant.HTTP_MESSAGE.FAILED);
            return response;
        }
        RatingEntity rating = ratingEntity.get();
        rating.setDeleted(true);
        rating.setModifiedDate(LocalDateTime.now());
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        rating.setModifiedBy(authentication.getName());
        ratingRepository.save(rating);
        response.setCode(HttpStatus.OK.value());
        response.setMessage(Constant.HTTP_MESSAGE.SUCCESS);
        response.setData(ratingMapper.toDto(rating));
        return response;
    }

    @Override
    public BaseResponse<?> findById(Long id) {
        BaseResponse<RatingDTO> response = new BaseResponse<>();
        Optional<RatingEntity> ratingEntity = ratingRepository.findById(id);
        if (ratingEntity.isEmpty()){
            response.setCode(HttpStatus.BAD_REQUEST.value());
            response.setMessage(Constant.HTTP_MESSAGE.FAILED);
            return response;
        }
        RatingEntity rating = ratingEntity.get();
        if (rating.getDeleted()){
            response.setCode(HttpStatus.BAD_REQUEST.value());
            response.setMessage(Constant.HTTP_MESSAGE.FAILED);
            return response;
        }
        RatingDTO ratingDTO = ratingMapper.toDto(ratingEntity.get());
        response.setCode(HttpStatus.OK.value());
        response.setMessage(Constant.HTTP_MESSAGE.SUCCESS);
        response.setData(ratingDTO);
        return response;
    }
}
