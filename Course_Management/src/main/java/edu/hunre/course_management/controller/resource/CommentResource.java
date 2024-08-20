package edu.hunre.course_management.controller.resource;

import edu.hunre.course_management.model.dto.CommentDTO;
import edu.hunre.course_management.model.dto.RatingDTO;
import edu.hunre.course_management.model.response.BaseResponse;
import edu.hunre.course_management.service.ICommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/comment")
public class CommentResource {
    @Autowired
    private ICommentService icommentService;

    @GetMapping("/list")
    public ResponseEntity<BaseResponse<Page<CommentDTO>>> getAll(
            @RequestParam(name = "page", required = false, defaultValue = "0") int page,
            @RequestParam(name = "size", required = false, defaultValue = "10") int size) {


        CommentDTO filterRequest = new CommentDTO();
        return ResponseEntity.ok(icommentService.getAll(filterRequest, page, size));
    }
    @PostMapping("/create")
    public ResponseEntity<BaseResponse<?>> create(@RequestBody CommentDTO commentDTO) {
        BaseResponse<?> response = icommentService.addComment(commentDTO);
        if (response.getCode() == HttpStatus.OK.value()){
            return ResponseEntity.ok(response);
        }else {
            return ResponseEntity.status(response.getCode()).body(response);
        }
    }
    @PutMapping("/update/{id}")
    public ResponseEntity<BaseResponse<?>> update(@RequestBody CommentDTO commentDTO, @PathVariable Long id) {
        BaseResponse<?> response = icommentService.updateComment(id, commentDTO);
        if (response.getCode() == HttpStatus.OK.value()){
            return ResponseEntity.ok(response);
        }else {
            return ResponseEntity.status(response.getCode()).body(response);
        }
    }
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<BaseResponse<?>> delete(@PathVariable Long id) {
        BaseResponse<?> response = icommentService.deleteComment(id);
        if (response.getCode() == HttpStatus.OK.value()){
            return ResponseEntity.ok(response);
        }else {
            return ResponseEntity.status(response.getCode()).body(response);
        }
    }
    @GetMapping("/findById/{id}")
    public ResponseEntity<BaseResponse<?>> getById(@PathVariable Long id) {
        BaseResponse<?> response = icommentService.findCommentById(id);
        if (response.getCode() == HttpStatus.OK.value()){
            return ResponseEntity.status(response.getCode()).body(response);
        }
        return ResponseEntity.status(response.getCode()).body(response);
    }
    @GetMapping("/countComment/{accountId}")
    public ResponseEntity<BaseResponse<?>> getAllSourse(@PathVariable Long accountId) {
        BaseResponse<Long> response = icommentService.countComment(accountId);
        if (response.getCode() == HttpStatus.OK.value()) {
            return new ResponseEntity<>(response, HttpStatus.OK);
        }else {
            return new ResponseEntity<>(response,HttpStatus.BAD_REQUEST);
        }
    }
}
