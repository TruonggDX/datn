package edu.hunre.course_management.controller.resource;

import edu.hunre.course_management.model.dto.CartDTO;
import edu.hunre.course_management.model.request.CartFilterRequest;
import edu.hunre.course_management.model.response.BaseResponse;
import edu.hunre.course_management.service.ICartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/cart")
public class CartResource {
    @Autowired
    private ICartService iCartService;
    @GetMapping("/list")
    public ResponseEntity<BaseResponse<Page<CartDTO>>> getCartItems(
            @RequestParam(name = "page", required = false, defaultValue = "0") int page,
            @RequestParam(name = "size", required = false, defaultValue = "10") int size) {
        BaseResponse<Page<CartDTO>> response = iCartService.getCourseByCustomerId(page, size);
        return new ResponseEntity<>(response, HttpStatus.valueOf(response.getCode()));
    }
    @GetMapping("/count")
    public ResponseEntity<BaseResponse<Long>> countCartItems() {
        BaseResponse<Long> response = iCartService.countCourse();
        return ResponseEntity.ok(response);
    }
    @PostMapping("/add")
    public ResponseEntity<BaseResponse<?>> addCartItem(@RequestBody CartDTO cartDTO) {
        BaseResponse<?> response = iCartService.addCourseInCart(cartDTO);
        return ResponseEntity.ok(response);
    }
    @GetMapping("/findCartById/{id}")
    public ResponseEntity<BaseResponse<?>> findCartById(@PathVariable  Long id) {
        BaseResponse<?> response = iCartService.findCourseById(id);
        if (response.getCode() == HttpStatus.OK.value()) {
            return ResponseEntity.ok(response);
        }else {
            return ResponseEntity.status(response.getCode()).body(response);
        }
    }
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<BaseResponse<?>> deleteCartItem(@PathVariable Long id) {
        BaseResponse<?> response = iCartService.deleteCourseInCart(id);
        if (response.getCode() == HttpStatus.OK.value()) {
            return ResponseEntity.ok(response);
        }else {
            return ResponseEntity.status(response.getCode()).body(response);
        }
    }
}
