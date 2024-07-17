package edu.hunre.course_management.controller.resource;

import edu.hunre.course_management.exception.ResourceNotFoundException;
import edu.hunre.course_management.model.dto.AccountDTO;
import edu.hunre.course_management.model.response.BaseResponse;
import edu.hunre.course_management.service.IAccountService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping(value = "/api/account")
public class AccountResource {
    @Autowired
    private IAccountService iAccountService;

    @GetMapping("/admin/list")
    public ResponseEntity<BaseResponse<Page<AccountDTO>>> getAll(@RequestParam(name = "page", required = false, defaultValue = "0") int page,
                                                                 @Min(10) @RequestParam(name = "size", required = false, defaultValue = "10") int size) {
        return ResponseEntity.ok(iAccountService.getAllUsers(page, size));
    }

    @PostMapping("/admin/create")
    public ResponseEntity<BaseResponse<?>> createUser(@Valid @RequestBody AccountDTO userDTO) {
        BaseResponse<?> response = iAccountService.createUser(userDTO);
        return new ResponseEntity<>(response, HttpStatus.valueOf(response.getCode()));
    }

//    @PutMapping("/admin/update/{id}")
//    public ResponseEntity<BaseResponse<AccountDTO>> updateUser(@PathVariable Long id, @RequestBody AccountDTO userDTO) {
//        BaseResponse<AccountDTO> response = iAccountService.updateUser(id, userDTO);
//        return ResponseEntity.status(response.getCode()).body(response);
//    }

//    @PostMapping("/admin/{accountId}")
//    public ResponseEntity<BaseResponse<?>> addCertificate(
//            @PathVariable Long accountId,
//            @RequestBody CerAccountRequest cerAccountRequest) {
//        BaseResponse<?> response = iAccountService.addCertificate(accountId, cerAccountRequest);
//        return new ResponseEntity<>(response, HttpStatus.valueOf(response.getCode()));
//    }

    @PutMapping("/admin/update/{id}")
    public ResponseEntity<BaseResponse<?>> updateCustomer(
            @PathVariable Long id,
            @ModelAttribute AccountDTO accountDto,
            @RequestParam(required = false) MultipartFile imageFile
    ) {
        BaseResponse<?> response = iAccountService.updateUser(id, accountDto, imageFile);
        return ResponseEntity.status(response.getCode()).body(response);
    }

    @DeleteMapping("/admin/delete/{userId}")
    public ResponseEntity<BaseResponse<?>> delete(@PathVariable @Min(value = 1, message = "userId must be greater than 0") Long userId) {
        try {
            iAccountService.deletedUser(userId);
            return ResponseEntity
                    .status(HttpStatus.NO_CONTENT)
                    .body(new BaseResponse<>(HttpStatus.NO_CONTENT.value(), "User deleted successfully", null));
        } catch (ResourceNotFoundException e) {
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body(new BaseResponse<>(HttpStatus.NOT_FOUND.value(), e.getMessage(), null));
        }
    }

    @GetMapping("/admin/search/{id}")
    public ResponseEntity<BaseResponse<AccountDTO>> findAccountById(@PathVariable Long id) {
        BaseResponse<AccountDTO> response = iAccountService.findAccountById(id);
        if (response.getCode() == HttpStatus.OK.value()) {
            return ResponseEntity.status(response.getCode()).body(response);
        }
        return ResponseEntity.notFound().build();
    }
    @GetMapping("/admin/searchByKey/{keyword}")
    public ResponseEntity<BaseResponse<List<AccountDTO>>> findAccByUserNameAndFullName(@PathVariable String keyword) {
        BaseResponse<List<AccountDTO>> response = iAccountService.findUserByUsAndFn(keyword);
        if (response.getCode()==HttpStatus.OK.value()) {
            return ResponseEntity.ok(response);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }
    }

    @GetMapping("/common/getUser")
    public ResponseEntity<BaseResponse<AccountDTO>> getCurrentUser() {
        BaseResponse<AccountDTO> response = iAccountService.getUser();
        if (response.getCode() == HttpStatus.OK.value()) {
            return ResponseEntity.ok(response);
        } else {
            return ResponseEntity.status(response.getCode()).body(response);
        }
    }
}
