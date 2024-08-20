package edu.hunre.course_management.service.impl;

import edu.hunre.course_management.entity.*;
import edu.hunre.course_management.mapper.CommentMapper;
import edu.hunre.course_management.model.dto.CommentDTO;
import edu.hunre.course_management.model.dto.RatingDTO;
import edu.hunre.course_management.model.response.BaseResponse;
import edu.hunre.course_management.repository.*;
import edu.hunre.course_management.service.ICommentService;
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
public class ICommentImpl implements ICommentService {
    @Autowired
    private CommentMapper commentMapper;
    @Autowired
    private CommentRepository commentRepository;
    @Autowired
    private CourseRepository courseRepository;
    @Autowired
    private RatingRepository ratingRepository;
    @Autowired
    private CustomerRepository customerRepository;
    @Autowired
    private AccountRepository accountRepository;

    @Override
    public BaseResponse<Page<CommentDTO>> getAll(CommentDTO commentDTO, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<CommentEntity> pages = commentRepository.findAllByFilter(commentDTO, pageable);
        List<CommentDTO> commentDTOList = pages.getContent().stream().map(commentMapper::toDto).toList();
        BaseResponse<Page<CommentDTO>> response = new BaseResponse<>();
        response.setCode(HttpStatus.OK.value());
        response.setMessage(Constant.HTTP_MESSAGE.SUCCESS);
        response.setData(new PageImpl<>(commentDTOList, pageable, pages.getTotalElements()));
        return response;

    }

    @Override
    public BaseResponse<?> addComment(CommentDTO commentDTO) {
        BaseResponse<CommentDTO> response = new BaseResponse<>();
        Optional<CustomerEntity> customer = customerRepository.findById(commentDTO.getCustomerId());
        Optional<CourseEntity> course = courseRepository.findById(commentDTO.getCourseId());
        Optional<RatingEntity> rating = ratingRepository.findById(commentDTO.getRateId());
        if (customer.isEmpty() || course.isEmpty() || rating.isEmpty()){
            response.setCode(HttpStatus.BAD_REQUEST.value());
            response.setMessage(Constant.HTTP_MESSAGE.FAILED);
            return response;
        }

        CommentEntity commentEntity = commentMapper.toEntity(commentDTO);
        commentEntity.setDeleted(false);
        commentEntity.setCourseEntity(course.get());
        commentEntity.setCustomerEntity(customer.get());
        commentEntity.setRatingEntity(rating.get());
        commentEntity.setCreatedDate(LocalDateTime.now());
        Authentication authentication = SecurityContextHolder.createEmptyContext().getAuthentication();
        commentEntity.setCreatedBy(authentication.getName());
        commentRepository.save(commentEntity);
        commentDTO = commentMapper.toDto(commentEntity);
        response.setCode(HttpStatus.OK.value());
        response.setMessage(Constant.HTTP_MESSAGE.SUCCESS);
        response.setData(commentDTO);
        return response;
    }

    @Override
    public BaseResponse<?> updateComment(Long id, CommentDTO commentDTO) {
        BaseResponse<CommentDTO> response = new BaseResponse<>();
        Optional<CommentEntity> comment = commentRepository.findById(id);
        if (comment.isEmpty()){
            response.setCode(HttpStatus.BAD_REQUEST.value());
            response.setMessage(Constant.HTTP_MESSAGE.FAILED);
            return response;
        }

        Optional<CustomerEntity> customer = customerRepository.findById(commentDTO.getCustomerId());
        Optional<CourseEntity> course = courseRepository.findById(commentDTO.getCourseId());
        Optional<RatingEntity> rating = ratingRepository.findById(commentDTO.getRateId());
        if (customer.isEmpty() || course.isEmpty() || rating.isEmpty()){
            response.setCode(HttpStatus.BAD_REQUEST.value());
            response.setMessage(Constant.HTTP_MESSAGE.FAILED);
            return response;
        }

        CommentEntity commentEntity = comment.get();
        commentEntity.setCustomerEntity(customer.get());
        commentEntity.setCourseEntity(course.get());
        commentEntity.setRatingEntity(rating.get());
        commentEntity.setModifiedDate(LocalDateTime.now());
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        commentEntity.setModifiedBy(authentication.getName());
        commentEntity.setDeleted(false);
        commentRepository.save(commentEntity);
        commentDTO = commentMapper.toDto(commentEntity);
        response.setCode(HttpStatus.OK.value());
        response.setMessage(Constant.HTTP_MESSAGE.SUCCESS);
        response.setData(commentDTO);

        return response;
    }

    @Override
    public BaseResponse<?> deleteComment(Long id) {
        BaseResponse<CommentDTO> response = new BaseResponse<>();
        Optional<CommentEntity> comment = commentRepository.findById(id);
        if (comment.isEmpty()){
            response.setCode(HttpStatus.BAD_REQUEST.value());
            response.setMessage(Constant.HTTP_MESSAGE.FAILED);
            return response;
        }
        CommentEntity commentEntity = comment.get();
        commentEntity.setDeleted(true);
        commentEntity.setModifiedDate(LocalDateTime.now());
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        commentEntity.setModifiedBy(authentication.getName());
        commentRepository.save(commentEntity);
        response.setCode(HttpStatus.OK.value());
        response.setMessage(Constant.HTTP_MESSAGE.SUCCESS);
        response.setData(commentMapper.toDto(commentEntity));
        return response;
    }

    @Override
    public BaseResponse<?> findCommentById(Long id) {
        BaseResponse<CommentDTO> response = new BaseResponse<>();
        Optional<CommentEntity> comment = commentRepository.findById(id);
        if (comment.isEmpty()){
            response.setCode(HttpStatus.BAD_REQUEST.value());
            response.setMessage(Constant.HTTP_MESSAGE.FAILED);
            return response;
        }
        CommentEntity commentEntity = comment.get();
        if (commentEntity.getDeleted()){
            response.setCode(HttpStatus.BAD_REQUEST.value());
            response.setMessage(Constant.HTTP_MESSAGE.FAILED);
            return response;
        }
        response.setCode(HttpStatus.OK.value());
        response.setMessage(Constant.HTTP_MESSAGE.SUCCESS);
        response.setData(commentMapper.toDto(commentEntity));
        return response;
    }

    @Override
    public BaseResponse<Long> countComment(Long accountId) {
        BaseResponse<Long> response = new BaseResponse<>();
        Optional<AccountEntity> accountEntity = accountRepository.findById(accountId);
        if (accountEntity.isEmpty()){
            response.setMessage(Constant.HTTP_MESSAGE.FAILED);
            response.setCode(HttpStatus.BAD_REQUEST.value());
            response.setData(null);
        }
        Long count = commentRepository.countCommentsByAccountId(accountId);
        response.setCode(HttpStatus.OK.value());
        response.setMessage(Constant.HTTP_MESSAGE.SUCCESS);
        response.setData(count);
        return response;
    }
}
