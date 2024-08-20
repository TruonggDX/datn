package edu.hunre.course_management.service;

import edu.hunre.course_management.entity.LevelEntity;
import edu.hunre.course_management.model.dto.CourseDTO;
import edu.hunre.course_management.model.dto.LevelCourseCountDTO;
import edu.hunre.course_management.model.request.CourseFilterRequest;
import edu.hunre.course_management.model.response.BaseResponse;
import org.springframework.data.domain.Page;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Map;

public interface ICourseService {
    BaseResponse<Page<CourseDTO>> getAll(CourseFilterRequest filterRequest, int page, int size);
    BaseResponse<?> addCourse(CourseDTO courseDTO, MultipartFile[] imageFiles) throws IOException;
    BaseResponse<?> updateCourse(Long id,CourseDTO courseDTO,MultipartFile[] imageFiles) throws IOException;
    BaseResponse<?> deleteCourse(Long id);
    BaseResponse<?> findById(Long id);
    BaseResponse<List<CourseDTO>> findCourseByName(String name);
    BaseResponse<Page<CourseDTO>> findCourseByCategoryId(Map<String,String> params, int page, int size);

    BaseResponse<List<LevelCourseCountDTO>> countCoursesByLevel(List<Long> levelIds);

    BaseResponse<Page<CourseDTO>> getAllCourseWithList(int page, int size);
    BaseResponse<Page<CourseDTO>> getAllCourseByAccountId(Long accountId,int page, int size);
    BaseResponse<Long> countCourseByAccountId(Long accountId);
    BaseResponse<Page<CourseDTO>> getCourseByLevelId(Long levelId,int page,int size);
}
