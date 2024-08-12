package edu.hunre.course_management.controller.resource;

import edu.hunre.course_management.model.dto.CourseDTO;
import edu.hunre.course_management.model.dto.OrderDTO;
import edu.hunre.course_management.model.request.CourseFilterRequest;
import edu.hunre.course_management.model.request.OrderFilterRequest;
import edu.hunre.course_management.model.response.BaseResponse;
import edu.hunre.course_management.service.IOrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/order")
public class OrderResource {
    @Autowired
    private IOrderService iOrderService;
    @GetMapping("/list")
    public ResponseEntity<BaseResponse<Page<OrderDTO>>> getAll(
            @RequestParam(name = "page", required = false, defaultValue = "0") int page,
            @RequestParam(name = "size", required = false, defaultValue = "10") int size) {


        OrderFilterRequest filterRequest = new OrderFilterRequest();
        return ResponseEntity.ok(iOrderService.getAll(filterRequest, page, size));
    }
    @PostMapping("/create")
    public ResponseEntity<BaseResponse<?>> create(@RequestBody OrderDTO orderDTO) {
        BaseResponse<?> response = iOrderService.createOrder(orderDTO);
        if (response.getCode() == HttpStatus.OK.value()) {
            return ResponseEntity.ok(response);
        }else {
            return ResponseEntity.status(response.getCode()).body(response);
        }
    }
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<BaseResponse<?>> delete(@PathVariable Long id) {
        BaseResponse<?> response = iOrderService.deleteOrder(id);
        if (response.getCode() == HttpStatus.OK.value()) {
            return ResponseEntity.ok(response);
        }else {
            return ResponseEntity.status(response.getCode()).body(response);
        }
    }
    @GetMapping("/findById/{id}")
    public ResponseEntity<BaseResponse<?>> getById(@PathVariable Long id) {
        BaseResponse<?> response = iOrderService.findById(id);
        if (response.getCode() == HttpStatus.OK.value()) {
            return ResponseEntity.ok(response);
        }else {
            return ResponseEntity.status(response.getCode()).body(response);
        }
    }
}
