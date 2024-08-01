package edu.hunre.course_management.controller.resource;


import com.fasterxml.jackson.core.JsonProcessingException;
import edu.hunre.course_management.model.dto.CourseDTO;
import edu.hunre.course_management.model.request.CourseFilterRequest;
import edu.hunre.course_management.model.response.BaseResponse;
import edu.hunre.course_management.service.ICourseService;
import jakarta.validation.constraints.Min;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("/api/course")
public class CourseResource {
    @Autowired
    private ICourseService icourseService;

    @GetMapping("/list")
    public ResponseEntity<BaseResponse<Page<CourseDTO>>> getAll(
            @RequestParam(name = "page", required = false, defaultValue = "0") int page,
            @RequestParam(name = "size", required = false, defaultValue = "10") int size) {


        CourseFilterRequest filterRequest = new CourseFilterRequest();
        return ResponseEntity.ok(icourseService.getAll(filterRequest, page, size));
    }

    @PostMapping("/create")
    public ResponseEntity<BaseResponse<?>> addCourse(
            @ModelAttribute CourseDTO courseDTO,
            @RequestParam(required = false) MultipartFile[] imageFiles) {
        try {
            BaseResponse<?> response = icourseService.addCourse(courseDTO, imageFiles);
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (IOException e) {
            e.printStackTrace();
            return new ResponseEntity<>(new BaseResponse<>(HttpStatus.INTERNAL_SERVER_ERROR.value(), "File upload error", null), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    @PutMapping("/update/{id}")
    public ResponseEntity<BaseResponse<?>> updateCourse(
            @PathVariable Long id,
            @ModelAttribute CourseDTO courseDTO,
            @RequestParam(required = false) MultipartFile[] imageFiles) {
        try {
            BaseResponse<?> response = icourseService.updateCourse(id, courseDTO, imageFiles);
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (IOException e) {
            e.printStackTrace();
            return new ResponseEntity<>(new BaseResponse<>(HttpStatus.INTERNAL_SERVER_ERROR.value(), "File upload error", null), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping("/delete/{id}")
    private ResponseEntity<BaseResponse<?>> deleteCourse(@PathVariable Long id) {
        BaseResponse<?> response = icourseService.deleteCourse(id);
        if (response.getCode() == HttpStatus.OK.value()) {
            return new ResponseEntity<>(response, HttpStatus.OK);
        }else {
            return new ResponseEntity<>(response,HttpStatus.BAD_REQUEST);
        }
    }
    @GetMapping("/findById/{id}")
    private ResponseEntity<BaseResponse<?>> findCourseById(@PathVariable Long id) {
        BaseResponse<?> response = icourseService.findById(id);
        if (response.getCode() == HttpStatus.OK.value()) {
            return new ResponseEntity<>(response, HttpStatus.OK);
        }else {
            return new ResponseEntity<>(response,HttpStatus.BAD_REQUEST);
        }
    }
}
