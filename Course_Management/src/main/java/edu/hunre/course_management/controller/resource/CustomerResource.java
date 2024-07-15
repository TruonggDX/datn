package edu.hunre.course_management.controller.resource;


import edu.hunre.course_management.model.dto.CustomerDTO;
import edu.hunre.course_management.model.request.ChagePasswordDTO;
import edu.hunre.course_management.model.request.RegisterDTO;
import edu.hunre.course_management.model.response.BaseResponse;
import edu.hunre.course_management.service.ICustomerService;
import jakarta.validation.constraints.Min;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;


@RestController
@RequestMapping(value = "/api/customer")
public class CustomerResource {
    @Autowired
    private ICustomerService iCustomerService;

    @GetMapping("/admin/list")
    public ResponseEntity<BaseResponse<Page<CustomerDTO>>> getAll(@RequestParam(name = "page", required = false, defaultValue = "0") int page,
                                                                  @Min(10) @RequestParam(name = "size", required = false, defaultValue = "10") int size) {
        return ResponseEntity.ok(iCustomerService.getAllCustomer(page, size));
    }

    @PostMapping("/admin/create")
    public ResponseEntity<BaseResponse<?>> createCustomer(@RequestBody CustomerDTO customerDTO) {
        BaseResponse<?> response = iCustomerService.addCustomer(customerDTO);
        return new ResponseEntity<>(response, HttpStatus.valueOf(response.getCode()));
    }


    @PutMapping("/common/update/{id}")
    public ResponseEntity<BaseResponse<?>> updateCustomer(
            @PathVariable Long id,
            @ModelAttribute CustomerDTO customerDTO,
            @RequestParam(required = false) MultipartFile imageFile
    ) {
        BaseResponse<?> response = iCustomerService.updateCustomer(id, customerDTO, imageFile);
        return ResponseEntity.status(response.getCode()).body(response);
    }



    @DeleteMapping("/admin/delete/{customerId}")
    public ResponseEntity<?> deleteCustomer(@PathVariable Long customerId) {
        BaseResponse<?> response = iCustomerService.deleteCustomerByID(customerId);
        if (response.getCode() == HttpStatus.OK.value()) {
            return ResponseEntity.ok().body(response);
        } else {
            return ResponseEntity.status(response.getCode()).body(response.getMessage());
        }
    }

    @GetMapping("/admin/search/{id}")
    public ResponseEntity<BaseResponse<?>> findAccountById(@PathVariable Long id) {
        BaseResponse<?> response = iCustomerService.findCustomerByID(id);
        if (response.getCode() == HttpStatus.OK.value()) {
            return ResponseEntity.status(response.getCode()).body(response);
        }
        return ResponseEntity.notFound().build();
    }

    @GetMapping("/admin/searchByCondition/{keyword}")
    public ResponseEntity<BaseResponse<List<CustomerDTO>>> findCusByUserNameAndFullName(@PathVariable String keyword) {
        BaseResponse<List<CustomerDTO>> response = iCustomerService.findCustomerByUsernameAndFullname(keyword);
        if (response.getCode() == HttpStatus.OK.value()) {
            return ResponseEntity.ok(response);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }
    }

    @PostMapping("/register")
    public BaseResponse<RegisterDTO> registerCustomer(@RequestBody RegisterDTO registerDTO) {
        return iCustomerService.registerCustomer(registerDTO);
    }

    @GetMapping("/common/getUser")
    public ResponseEntity<BaseResponse<CustomerDTO>> getCurrentUser() {
        BaseResponse<CustomerDTO> response = iCustomerService.getUser();
        if (response.getCode() == HttpStatus.OK.value()) {
            return ResponseEntity.ok(response);
        } else {
            return ResponseEntity.status(response.getCode()).body(response);
        }
    }

    @PutMapping("/updatePassWord/{id}")
    public ResponseEntity<BaseResponse<?>> updatePass(@PathVariable Long id, @RequestBody ChagePasswordDTO chagePasswordDTO){
        BaseResponse<?> response = iCustomerService.updatePassWord(id,chagePasswordDTO);
        if (response.getCode() == HttpStatus.OK.value()) {
            return ResponseEntity.ok(response);
        }else {
            return ResponseEntity.status(response.getCode()).body(response);
        }
    }
}
