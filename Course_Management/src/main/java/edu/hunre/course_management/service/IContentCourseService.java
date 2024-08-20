package edu.hunre.course_management.service;

import edu.hunre.course_management.model.dto.ContentCourseDTO;
import edu.hunre.course_management.model.request.ContentCourseRequest;
import edu.hunre.course_management.model.response.BaseResponse;
import org.springframework.data.domain.Page;

public interface IContentCourseService {
    BaseResponse<Page<ContentCourseDTO>> findAll(int page,int size);
    BaseResponse<ContentCourseRequest> create(ContentCourseRequest contentCourseRequest);
    BaseResponse<ContentCourseRequest> update(Long id,ContentCourseRequest contentCourseRequest);
}
