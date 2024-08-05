package edu.hunre.course_management.controller.resource;

import edu.hunre.course_management.model.dto.CourseDTO;
import edu.hunre.course_management.model.dto.RatingDTO;
import edu.hunre.course_management.model.request.CourseFilterRequest;
import edu.hunre.course_management.model.response.BaseResponse;
import edu.hunre.course_management.service.IRatingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/rating")
public class RatingResource {
    @Autowired
    private IRatingService iratingService;
    @GetMapping("/list")
    public ResponseEntity<BaseResponse<Page<RatingDTO>>> getAll(
            @RequestParam(name = "page", required = false, defaultValue = "0") int page,
            @RequestParam(name = "size", required = false, defaultValue = "10") int size) {


        RatingDTO filterRequest = new RatingDTO();
        return ResponseEntity.ok(iratingService.getAll(filterRequest, page, size));
    }
    @PostMapping("/create")
    public ResponseEntity<BaseResponse<?>> create(@RequestBody RatingDTO ratingDTO) {
        BaseResponse<?> response  = iratingService.addRating(ratingDTO);
        return ResponseEntity.ok(response);
    }
    @PutMapping("/update/{id}")
    public ResponseEntity<BaseResponse<?>> update(@PathVariable Long id, @RequestBody RatingDTO ratingDTO) {
        BaseResponse<?> response = iratingService.updateRating(id,ratingDTO);
        return ResponseEntity.ok(response);
    }
    @GetMapping("/findById/{id}")
    private ResponseEntity<BaseResponse<?>> findById(@PathVariable Long id) {
        BaseResponse<?> response = iratingService.findById(id);
        if (response.getCode() == HttpStatus.OK.value()) {
            return new ResponseEntity<>(response, HttpStatus.OK);
        }else {
            return new ResponseEntity<>(response,HttpStatus.BAD_REQUEST);
        }
    }
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<BaseResponse<?>> delete(@PathVariable Long id) {
        BaseResponse<?> response = iratingService.deleteRating(id);
        if (response.getCode() == HttpStatus.OK.value()) {
            return new ResponseEntity<>(response, HttpStatus.OK);
        }else {
            return new ResponseEntity<>(response,HttpStatus.BAD_REQUEST);
        }
    }
}
