package edu.hunre.course_management.controller.resource;

import edu.hunre.course_management.exception.ResourceNotFoundException;
import edu.hunre.course_management.model.dto.RoleDTO;
import edu.hunre.course_management.model.dto.UserDTO;
import edu.hunre.course_management.model.response.BaseResponse;
import edu.hunre.course_management.service.IRoleService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/role")
public class RoleResource {
    @Autowired
    private IRoleService iRoleService;
    @GetMapping("/list")
    public ResponseEntity<BaseResponse<Page<RoleDTO>>> getAll(@RequestParam(name = "page", required = false, defaultValue = "0") int page,
                                                              @RequestParam(name = "size", required = false, defaultValue = "10") int size) {
        return ResponseEntity.ok(iRoleService.getAll(page, size));
    }
    @PostMapping("/create")
    public ResponseEntity<BaseResponse<?>> createUser(@Valid @RequestBody RoleDTO roleDTO) {
        BaseResponse<?> response = iRoleService.createRole(roleDTO);
        return new ResponseEntity<>(response, HttpStatus.valueOf(response.getCode()));
    }
    @PutMapping("/update/{id}")
    public ResponseEntity<BaseResponse<RoleDTO>> updateUser(@PathVariable Long id,  @RequestBody RoleDTO roleDTO) {
        BaseResponse<RoleDTO> response = iRoleService.updateRole(id, roleDTO);
        return ResponseEntity.status(response.getCode()).body(response);
    }

    @DeleteMapping("/delete/{id}")
    public BaseResponse<?> deleteSize(@PathVariable Long id) {
        BaseResponse<?> response = iRoleService.deleteRole(id);
        return response;
    }

    @GetMapping("/search/{id}")
    public ResponseEntity<BaseResponse<RoleDTO>> findAccountById(@PathVariable Long id) {
        BaseResponse<RoleDTO> response = iRoleService.findByIdRole(id);
        if (response.getCode() == HttpStatus.OK.value()) {
            return ResponseEntity.status(response.getCode()).body(response);
        }
        return ResponseEntity.notFound().build();
    }
}
