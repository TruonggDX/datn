package edu.hunre.course_management.service;

import edu.hunre.course_management.model.dto.ImageCourseDTO;
import edu.hunre.course_management.model.response.BaseResponse;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface IImageCourseService {
    BaseResponse<List<ImageCourseDTO>> uploadFiles(List<MultipartFile> files) throws IOException;
    BaseResponse<?> deleteImage(Long imageId);
    BaseResponse<List<ImageCourseDTO>> updateImages(List<Long> imageIds, List<MultipartFile> newFiles) throws IOException;
}
