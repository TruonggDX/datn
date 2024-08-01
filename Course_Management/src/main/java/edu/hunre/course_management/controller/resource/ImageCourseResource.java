package edu.hunre.course_management.controller.resource;

import edu.hunre.course_management.model.dto.ImageCourseDTO;
import edu.hunre.course_management.model.dto.ImageDTO;
import edu.hunre.course_management.model.response.BaseResponse;
import edu.hunre.course_management.service.IImageCourseService;
import edu.hunre.course_management.service.IImageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api/image_course")
public class ImageCourseResource {
    @Autowired
    private IImageCourseService fileUploadService;

    @PostMapping("/upload")
    public ResponseEntity<BaseResponse<List<ImageCourseDTO>>> uploadFiles(@RequestParam("files") List<MultipartFile> files) {
        try {
            BaseResponse<List<ImageCourseDTO>> response = fileUploadService.uploadFiles(files);
            return ResponseEntity.ok(response);
        } catch (IOException e) {
            BaseResponse<List<ImageCourseDTO>> errorResponse = new BaseResponse<>();
            errorResponse.setMessage("Failed to upload files: " + e.getMessage());
            errorResponse.setCode(HttpStatus.INTERNAL_SERVER_ERROR.value());
            return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping("/update")
    public ResponseEntity<BaseResponse<List<ImageCourseDTO>>> updateImages(
            @RequestParam List<Long> imageIds,
            @RequestParam List<MultipartFile> newFiles) {
        try {
            BaseResponse<List<ImageCourseDTO>> response = fileUploadService.updateImages(imageIds, newFiles);
            return new ResponseEntity<>(response, HttpStatus.valueOf(response.getCode()));
        } catch (IOException e) {
            BaseResponse<List<ImageCourseDTO>> errorResponse = new BaseResponse<>();
            errorResponse.setCode(HttpStatus.INTERNAL_SERVER_ERROR.value());
            errorResponse.setMessage("File processing error: " + e.getMessage());
            return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

}
