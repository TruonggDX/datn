package edu.hunre.course_management.service;

import edu.hunre.course_management.model.dto.CourseDTO;
import edu.hunre.course_management.model.request.CourseFilterRequest;
import edu.hunre.course_management.model.response.BaseResponse;
import org.springframework.data.domain.Page;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface ICourseService {
    BaseResponse<Page<CourseDTO>> getAll(CourseFilterRequest filterRequest, int page, int size);
    BaseResponse<?> addCourse(CourseDTO courseDTO, MultipartFile[] imageFiles) throws IOException;
    BaseResponse<?> updateCourse(Long id,CourseDTO courseDTO,MultipartFile[] imageFiles) throws IOException;
    BaseResponse<?> deleteCourse(Long id);
    BaseResponse<?> findById(Long id);
}
