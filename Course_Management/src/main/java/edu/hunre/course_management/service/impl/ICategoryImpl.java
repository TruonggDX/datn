package edu.hunre.course_management.service.impl;

import edu.hunre.course_management.entity.CategoryEntity;
import edu.hunre.course_management.entity.RoleEntity;
import edu.hunre.course_management.mapper.CategoryMapper;
import edu.hunre.course_management.model.dto.CategoryDTO;
import edu.hunre.course_management.model.dto.RoleDTO;
import edu.hunre.course_management.model.response.BaseResponse;
import edu.hunre.course_management.repository.CategoryRepository;
import edu.hunre.course_management.service.ICategoryService;
import edu.hunre.course_management.utils.Constant;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ICategoryImpl implements ICategoryService {
    @Autowired
    private CategoryMapper categoryMapper;
    @Autowired
    private CategoryRepository categoryRepository;

    @Override
    public BaseResponse<Page<CategoryDTO>> getAllCategory(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<CategoryEntity> categoryPage = categoryRepository.findAllCategory(pageable);
        List<CategoryDTO> categoryDTOS = categoryPage.getContent()
                .stream()
                .map(categoryMapper::toDTO)
                .collect(Collectors.toList());

        BaseResponse<Page<CategoryDTO>> response = new BaseResponse<>();
        response.setMessage(Constant.HTTP_MESSAGE.SUCCESS);
        response.setCode(HttpStatus.OK.value());
        response.setData(new PageImpl<>(categoryDTOS, pageable, categoryPage.getTotalElements()));
        return response;
    }

    @Override
    public BaseResponse<List<CategoryDTO>> getSubcategories(Long parentId) {
        BaseResponse<List<CategoryDTO>> response = new BaseResponse<>();
        List<CategoryEntity> categoryEntities = categoryRepository.findByParentId(parentId)
                .stream()
                .filter(categoryEntity -> !categoryEntity.getDeleted())
                .collect(Collectors.toList());

        if (categoryEntities.isEmpty()) {
            response.setMessage(Constant.HTTP_MESSAGE.FAILED);
            response.setCode(HttpStatus.BAD_REQUEST.value());
            return response;
        }

        List<CategoryDTO> categoryDTOS = categoryEntities.stream()
                .map(categoryMapper::toDTO)
                .collect(Collectors.toList());

        response.setMessage(Constant.HTTP_MESSAGE.SUCCESS);
        response.setCode(HttpStatus.OK.value());
        response.setData(categoryDTOS);
        return response;
    }

    @Override
    public BaseResponse<CategoryDTO> findCategoryById(Long id) {
        BaseResponse<CategoryDTO> response=new BaseResponse<>();
        Optional<CategoryEntity> categoryEntity = categoryRepository.findById(id);
        if(categoryEntity.isEmpty() || categoryEntity.get().getDeleted()){
            response.setMessage(Constant.HTTP_MESSAGE.FAILED);
            response.setCode(HttpStatus.BAD_REQUEST.value());
            return response;
        }
        CategoryDTO categoryDTO = categoryMapper.toDTO(categoryEntity.get());
        response.setMessage(Constant.HTTP_MESSAGE.SUCCESS);
        response.setCode(HttpStatus.OK.value());
        response.setData(categoryDTO);
        return response;
    }

    @Override
    public BaseResponse<CategoryDTO> addCategory(CategoryDTO categoryDTO) {
        BaseResponse<CategoryDTO> response = new BaseResponse<>();

        CategoryEntity categoryEntity = categoryMapper.toEntity(categoryDTO);
        categoryEntity.setDeleted(false);

        if (categoryDTO.getParentId() != null) {
            Optional<CategoryEntity> parentCategory = categoryRepository.findById(categoryDTO.getParentId());
            if (parentCategory.isEmpty()) {
                response.setMessage(Constant.HTTP_MESSAGE.FAILED);
                response.setCode(HttpStatus.BAD_REQUEST.value());
                return response;
            }
            categoryEntity.setParent(parentCategory.get());
        } else {
            categoryEntity.setParent(null);
        }

        CategoryEntity savedEntity = categoryRepository.save(categoryEntity);
        CategoryDTO savedDTO = categoryMapper.toDTO(savedEntity);
        response.setMessage(Constant.HTTP_MESSAGE.SUCCESS);
        response.setCode(HttpStatus.OK.value());
        response.setData(savedDTO);
        return response;
    }


    @Override
    public BaseResponse<CategoryDTO> updateCategory(Long id, CategoryDTO categoryDTO) {
        BaseResponse<CategoryDTO> response = new BaseResponse<>();
        Optional<CategoryEntity> existingCategory = categoryRepository.findById(id);
        if (existingCategory.isEmpty() || existingCategory.get().getDeleted()) {
            response.setMessage(Constant.HTTP_MESSAGE.FAILED);
            response.setCode(HttpStatus.BAD_REQUEST.value());
            return response;
        }

        CategoryEntity categoryEntity = existingCategory.get();
        categoryEntity.setName(categoryDTO.getName());

        if (categoryDTO.getParentId() != null) {
            Optional<CategoryEntity> parentCategory = categoryRepository.findById(categoryDTO.getParentId());
            if (parentCategory.isEmpty()) {
                response.setMessage(Constant.HTTP_MESSAGE.FAILED);
                response.setCode(HttpStatus.BAD_REQUEST.value());
                return response;
            }
            categoryEntity.setParent(parentCategory.get());
        } else {
            categoryEntity.setParent(null);
        }

        CategoryEntity updatedEntity = categoryRepository.save(categoryEntity);

        CategoryDTO updatedDTO = categoryMapper.toDTO(updatedEntity);
        response.setMessage(Constant.HTTP_MESSAGE.SUCCESS);
        response.setCode(HttpStatus.OK.value());
        response.setData(updatedDTO);
        return response;
    }


    @Override
    public BaseResponse<CategoryDTO> deleteCategory(Long id) {
        BaseResponse<CategoryDTO> response = new BaseResponse<>();
        Optional<CategoryEntity> categoryEntity = categoryRepository.findById(id);
        if (categoryEntity.isEmpty() || categoryEntity.get().getDeleted()) {
            response.setMessage(Constant.HTTP_MESSAGE.FAILED);
            response.setCode(HttpStatus.BAD_REQUEST.value());
            return response;
        }
        CategoryEntity category = categoryEntity.get();
        List<CategoryEntity> childCategories = categoryRepository.findByParentId(id);
        for (CategoryEntity child : childCategories) {
            child.setDeleted(true);
            categoryRepository.save(child);
        }

        category.setDeleted(true);
        CategoryDTO categoryDTO = categoryMapper.toDTO(category);
        categoryRepository.save(category);

        response.setMessage(Constant.HTTP_MESSAGE.SUCCESS);
        response.setCode(HttpStatus.OK.value());
        response.setData(categoryDTO);

        return response;
    }


    @Override
    public BaseResponse<List<CategoryDTO>> getAllParentCategories() {
        BaseResponse<List<CategoryDTO>> response = new BaseResponse<>();
        List<CategoryDTO> parentCategories = categoryRepository.findAll().stream()
                .filter(categoryEntity -> categoryEntity.getParent() == null && !categoryEntity.getDeleted())
                .map(categoryMapper::toDTO)
                .collect(Collectors.toList());

        response.setData(parentCategories);
        response.setMessage(Constant.HTTP_MESSAGE.SUCCESS);
        response.setCode(HttpStatus.OK.value());
        return response;
    }
}
