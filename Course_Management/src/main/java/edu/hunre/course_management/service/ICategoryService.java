package edu.hunre.course_management.service;

import edu.hunre.course_management.model.dto.CategoryDTO;
import edu.hunre.course_management.model.response.BaseResponse;
import org.springframework.data.domain.Page;

import java.util.List;

public interface ICategoryService {
    BaseResponse<Page<CategoryDTO>> getAllCategory(int page,int size);
    BaseResponse<List<CategoryDTO>> getSubcategories(Long parentId);
    BaseResponse<CategoryDTO> findCategoryById(Long id);
    BaseResponse<CategoryDTO> addCategory(CategoryDTO categoryDTO);
    BaseResponse<CategoryDTO> updateCategory(Long id,CategoryDTO categoryDTO);
    BaseResponse<CategoryDTO> deleteCategory(Long id);
    BaseResponse<List<CategoryDTO>>getAllParentCategories();
}
