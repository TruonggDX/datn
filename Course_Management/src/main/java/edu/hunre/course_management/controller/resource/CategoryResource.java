package edu.hunre.course_management.controller.resource;

import edu.hunre.course_management.model.dto.AccountDTO;
import edu.hunre.course_management.model.dto.CategoryDTO;
import edu.hunre.course_management.model.response.BaseResponse;
import edu.hunre.course_management.service.ICategoryService;
import jakarta.validation.constraints.Min;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "api/category/")
public class CategoryResource {
    @Autowired
    private ICategoryService iCategoryService;
    @GetMapping("/admin/list")
    public ResponseEntity<BaseResponse<Page<CategoryDTO>>> getAll(@RequestParam(name = "page", required = false, defaultValue = "0") int page,
                                                                  @Min(10) @RequestParam(name = "size", required = false, defaultValue = "10") int size) {
        return ResponseEntity.ok(iCategoryService.getAllCategory(page, size));
    }
    @GetMapping("/admin/searchById/{id}")
    public ResponseEntity<BaseResponse<CategoryDTO>> findAccountById(@PathVariable Long id) {
        BaseResponse<CategoryDTO> response = iCategoryService.findCategoryById(id);
        if (response.getCode() == HttpStatus.OK.value()) {
            return ResponseEntity.status(response.getCode()).body(response);
        }
        return ResponseEntity.notFound().build();
    }
    @GetMapping("/admin/searchByParentId/{parentId}")
    public ResponseEntity<BaseResponse<List<CategoryDTO>>> findAccountByParentId(@PathVariable Long parentId) {
        BaseResponse<List<CategoryDTO>> response = iCategoryService.getSubcategories(parentId);
        if (response.getCode() == HttpStatus.OK.value()) {
            return ResponseEntity.status(response.getCode()).body(response);
        }
        return ResponseEntity.notFound().build();
    }
    @GetMapping("/admin/parents")
    public BaseResponse<List<CategoryDTO>> getAllParentCategories() {
        return iCategoryService.getAllParentCategories();
    }
    @PostMapping("/admin/create")
    public ResponseEntity<BaseResponse<CategoryDTO>> create(@RequestBody CategoryDTO categoryDTO) {
        BaseResponse<CategoryDTO> response = iCategoryService.addCategory(categoryDTO);
        if (response.getCode() == HttpStatus.CREATED.value()) {
            return ResponseEntity.status(response.getCode()).body(response);
        }else {
            return ResponseEntity.status(response.getCode()).body(response);
        }
    }
    @PutMapping("/admin/update/{id}")
    public ResponseEntity<BaseResponse<CategoryDTO>> update(@PathVariable Long id, @RequestBody CategoryDTO categoryDTO) {
        BaseResponse<CategoryDTO> response = iCategoryService.updateCategory(id, categoryDTO);
        if (response.getCode() == HttpStatus.OK.value()) {
            return ResponseEntity.status(response.getCode()).body(response);
        }else {
            return ResponseEntity.status(response.getCode()).body(response);
        }
    }
    @DeleteMapping("/admin/delete/{id}")
    public ResponseEntity<BaseResponse> delete(@PathVariable Long id) {
        BaseResponse response = iCategoryService.deleteCategory(id);
        if (response.getCode() == HttpStatus.OK.value()) {
            return ResponseEntity.status(response.getCode()).build();
        }else {
            return ResponseEntity.status(response.getCode()).body(response);
        }
    }

}
