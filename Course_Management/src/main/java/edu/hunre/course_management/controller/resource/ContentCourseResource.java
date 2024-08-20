package edu.hunre.course_management.controller.resource;

import edu.hunre.course_management.model.dto.ContentCourseDTO;
import edu.hunre.course_management.model.request.ContentCourseRequest;
import edu.hunre.course_management.model.response.BaseResponse;
import edu.hunre.course_management.service.IContentCourseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/content_course")
public class ContentCourseResource {
    @Autowired
    private IContentCourseService contentCourseService;
    @GetMapping("/list")
    public ResponseEntity<BaseResponse<Page<ContentCourseDTO>>> getAll(
            @RequestParam(name = "page", required = false, defaultValue = "0") int page,
            @RequestParam(name = "size", required = false, defaultValue = "10") int size) {
        return ResponseEntity.ok(contentCourseService.findAll(page, size));
    }
    @PostMapping("/create")
    public ResponseEntity<BaseResponse<?>> create(@RequestBody ContentCourseRequest contentCourseRequest) {
        BaseResponse<?> response = contentCourseService.create(contentCourseRequest);
        return ResponseEntity.ok(response);
    }
    @PutMapping("/update/{id}")
    public ResponseEntity<BaseResponse<?>> update(@PathVariable Long id, @RequestBody ContentCourseRequest contentCourseRequest) {
        BaseResponse<?> response = contentCourseService.update(id,contentCourseRequest);
        return ResponseEntity.ok(response);
    }
}
