package edu.hunre.course_management.service.impl;

import edu.hunre.course_management.entity.ContentCourseEntity;
import edu.hunre.course_management.mapper.ContentCourseMapper;
import edu.hunre.course_management.model.dto.ContentCourseDTO;
import edu.hunre.course_management.model.request.ContentCourseRequest;
import edu.hunre.course_management.model.response.BaseResponse;
import edu.hunre.course_management.repository.ContentCourseRepository;
import edu.hunre.course_management.repository.CourseRepository;
import edu.hunre.course_management.service.IContentCourseService;
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
public class IContentCourseImpl implements IContentCourseService {
    @Autowired
    private ContentCourseRepository contentCourseRepository;
    @Autowired
    private CourseRepository courseRepository;
    @Autowired
    private ContentCourseMapper contentCourseMapper;

    @Override
    public BaseResponse<Page<ContentCourseDTO>> findAll(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<ContentCourseEntity> contentCourseEntities = contentCourseRepository.findAllByDeletedFalse(pageable);
        List<ContentCourseDTO> contentCourseDTOS = contentCourseEntities.getContent().stream().map(contentCourseMapper::toDto).collect(Collectors.toList());
        BaseResponse<Page<ContentCourseDTO>> response = new BaseResponse<>();
        response.setCode(HttpStatus.OK.value());
        response.setMessage(Constant.HTTP_MESSAGE.SUCCESS);
        response.setData(new PageImpl<>(contentCourseDTOS, pageable, contentCourseEntities.getTotalElements()));
        return response;
    }

    @Override
    public BaseResponse<ContentCourseRequest> create(ContentCourseRequest contentCourseRequest) {
        BaseResponse<ContentCourseRequest> response = new BaseResponse<>();
        ContentCourseEntity contentCourseEntity = contentCourseMapper.toEntityRequest(contentCourseRequest);
        contentCourseEntity.setDeleted(false);
        contentCourseEntity.setCreatedDate(LocalDateTime.now());
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        contentCourseEntity.setCreatedBy(authentication.getName());
        contentCourseRepository.save(contentCourseEntity);
        response.setCode(HttpStatus.OK.value());
        response.setMessage(Constant.HTTP_MESSAGE.SUCCESS);
        response.setData(contentCourseMapper.toDtoRequest(contentCourseEntity));
        return response;
    }

    @Override
    public BaseResponse<ContentCourseRequest> update(Long id, ContentCourseRequest contentCourseRequest) {
        BaseResponse<ContentCourseRequest> response = new BaseResponse<>();
        Optional<ContentCourseEntity> optionalContentCourseEntity = contentCourseRepository.findById(id);

        if (optionalContentCourseEntity.isEmpty()) {
            response.setCode(HttpStatus.BAD_REQUEST.value());
            response.setMessage(Constant.HTTP_MESSAGE.FAILED);
            return response;
        }

        ContentCourseEntity existingEntity = optionalContentCourseEntity.get();
        existingEntity.setName(contentCourseRequest.getName());
        existingEntity.setContent(contentCourseRequest.getContent());
        existingEntity.setLink(contentCourseRequest.getLink());
        existingEntity.setDuration(contentCourseRequest.getDuration());
        existingEntity.setNumber(contentCourseRequest.getNumber());

        existingEntity.setDeleted(false);
        existingEntity.setModifiedDate(LocalDateTime.now());
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        existingEntity.setModifiedBy(authentication.getName());

        contentCourseRepository.save(existingEntity);

        response.setCode(HttpStatus.OK.value());
        response.setMessage(Constant.HTTP_MESSAGE.SUCCESS);
        response.setData(contentCourseMapper.toDtoRequest(existingEntity));
        return response;
    }

}
