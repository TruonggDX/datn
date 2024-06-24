package edu.hunre.course_management.controller.resource;

import edu.hunre.course_management.exception.ResourceNotFoundException;
import edu.hunre.course_management.model.dto.UserDTO;
import edu.hunre.course_management.model.response.BaseResponse;
import edu.hunre.course_management.service.IUserService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "/api/user")
public class UserResource {
    @Autowired
    private IUserService iUserService;

    @GetMapping("/list")
    public ResponseEntity<BaseResponse<Page<UserDTO>>> getAll(@RequestParam(name = "page", required = false, defaultValue = "0") int page,
                                                              @Min(10) @RequestParam(name = "size", required = false, defaultValue = "10") int size) {
        return ResponseEntity.ok(iUserService.getAllUsers(page, size));
    }

    @PostMapping("/create")
    public ResponseEntity<BaseResponse<?>> createUser(@Valid @RequestBody UserDTO userDTO) {
        BaseResponse<?> response = iUserService.createUser(userDTO);
        return new ResponseEntity<>(response, HttpStatus.valueOf(response.getCode()));
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<BaseResponse<UserDTO>> updateUser(@PathVariable Long id, @RequestBody UserDTO userDTO) {
        BaseResponse<UserDTO> response = iUserService.updateUser(id, userDTO);
        return ResponseEntity.status(response.getCode()).body(response);
    }

    @DeleteMapping("/delete/{userId}")
    public ResponseEntity<BaseResponse<?>> delete(@PathVariable @Min(value = 1, message = "userId must be greater than 0") Long userId) {
        try {
            iUserService.deletedUser(userId);
            return ResponseEntity
                    .status(HttpStatus.NO_CONTENT)
                    .body(new BaseResponse<>(HttpStatus.NO_CONTENT.value(), "User deleted successfully", null));
        } catch (ResourceNotFoundException e) {
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body(new BaseResponse<>(HttpStatus.NOT_FOUND.value(), e.getMessage(), null));
        }
    }

    @GetMapping("/search/{id}")
    public ResponseEntity<BaseResponse<UserDTO>> findAccountById(@PathVariable Long id) {
        BaseResponse<UserDTO> response = iUserService.findAccountById(id);
        if (response.getCode() == HttpStatus.OK.value()) {
            return ResponseEntity.status(response.getCode()).body(response);
        }
        return ResponseEntity.notFound().build();
    }
    @GetMapping("/searchByKey/{keyword}")
    public ResponseEntity<BaseResponse<List<UserDTO>>> findAccByUserNameAndFullName(@PathVariable String keyword) {
        BaseResponse<List<UserDTO>> response = iUserService.findUserByUsAndFn(keyword);
        if (response.getCode()==HttpStatus.OK.value()) {
            return ResponseEntity.ok(response);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }
    }

}
