package edu.hunre.course_management.service.impl;

import edu.hunre.course_management.entity.*;
import edu.hunre.course_management.mapper.CourseMapper;
import edu.hunre.course_management.mapper.ImageSourseMapper;
import edu.hunre.course_management.model.dto.CourseDTO;
import edu.hunre.course_management.model.dto.CustomerDTO;
import edu.hunre.course_management.model.dto.ImageCourseDTO;
import edu.hunre.course_management.model.dto.ImageDTO;
import edu.hunre.course_management.model.request.CourseFilterRequest;
import edu.hunre.course_management.model.response.BaseResponse;
import edu.hunre.course_management.repository.CategoryRepository;
import edu.hunre.course_management.repository.CourseRepository;
import edu.hunre.course_management.repository.ImageCourseRepository;
import edu.hunre.course_management.repository.LanguageRepository;
import edu.hunre.course_management.service.ICourseService;
import edu.hunre.course_management.service.IImageCourseService;
import edu.hunre.course_management.utils.Constant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class ICourseImpl implements ICourseService {
    @Autowired
    private ImageCourseRepository imageCourseRepository;
    @Autowired
    private CourseRepository courseRepository;
    @Autowired
    private CourseMapper courseMapper;
    @Autowired
    private IImageCourseService imageCourseService;
    @Autowired
    private ImageSourseMapper imageSourseMapper;
    @Autowired
    private CategoryRepository categoryRepository;
    @Autowired
    private LanguageRepository languageRepository;

    @Override
    public BaseResponse<Page<CourseDTO>> getAll(CourseFilterRequest filterRequest, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<CourseEntity> courseEntities = courseRepository.findAllByFilter(filterRequest, pageable);
        List<CourseDTO> courseDTOs = courseEntities.getContent().stream().map(courseMapper::toDTO).collect(Collectors.toList());
        BaseResponse<Page<CourseDTO>> response = new BaseResponse<>();
        response.setCode(HttpStatus.OK.value());
        response.setMessage(Constant.HTTP_MESSAGE.SUCCESS);
        response.setData(new PageImpl<>(courseDTOs, pageable, courseEntities.getTotalElements()));
        return response;
    }

    @Override
    public BaseResponse<?> addCourse(CourseDTO courseDTO, MultipartFile[] imageFiles) throws IOException {
        BaseResponse<CourseDTO> response = new BaseResponse<>();
        Optional<CategoryEntity> category = categoryRepository.findById(courseDTO.getCategoryId());
        if (category.isEmpty()) {
            response.setCode(HttpStatus.BAD_REQUEST.value());
            response.setMessage(Constant.HTTP_MESSAGE.FAILED);
            return response;
        }
        Optional<LanguageEntity> language = languageRepository.findById(courseDTO.getLanguageId());
        if (language.isEmpty()) {
            response.setCode(HttpStatus.BAD_REQUEST.value());
            response.setMessage(Constant.HTTP_MESSAGE.FAILED);
            return response;
        }
        CourseEntity courseEntity = courseMapper.toEntity(courseDTO);
        courseEntity.setDeleted(false);
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        courseEntity.setCreatedBy(authentication.getName());

        courseEntity.setCategoryEntity(category.get());
        courseEntity.setLanguageEntity(language.get());
        courseEntity = courseRepository.save(courseEntity);

        try {
            if (imageFiles != null) {
                List<MultipartFile> imageFileList = Arrays.asList(imageFiles);
                BaseResponse<List<ImageCourseDTO>> responses = imageCourseService.uploadFiles(imageFileList);
                List<ImageCourseDTO> uploadedImages = responses.getData();
                List<ImageCourseEntity> imageEntities = new ArrayList<>();
                for (ImageCourseDTO uploadedImage : uploadedImages) {
                    ImageCourseEntity imageEntity = imageSourseMapper.toEntity(uploadedImage);
                    imageEntity.setCourseEntity(courseEntity);
                    imageEntity.setDeleted(false);
                    imageEntity.setCreatedDate(LocalDateTime.now());
                    imageEntities.add(imageEntity);
                }
                imageCourseRepository.saveAll(imageEntities);
                courseEntity.setImageEntityList(imageEntities);
            }
        } catch (IOException e) {
            e.printStackTrace();
            response.setCode(HttpStatus.INTERNAL_SERVER_ERROR.value());
            response.setMessage(Constant.HTTP_MESSAGE.FAILED);
            return response;
        }
        
        courseDTO = courseMapper.toDTO(courseEntity);
        response.setCode(HttpStatus.OK.value());
        response.setMessage(Constant.HTTP_MESSAGE.SUCCESS);
        response.setData(courseDTO);
        return response;
    }



    @Override
    public BaseResponse<?> updateCourse(Long id, CourseDTO courseDTO, MultipartFile[] imageFiles) throws IOException {
        BaseResponse<CourseDTO> response = new BaseResponse<>();
        Optional<CourseEntity> optionalCourseEntity = courseRepository.findById(id);

        if (optionalCourseEntity.isEmpty()) {
            response.setCode(HttpStatus.BAD_REQUEST.value());
            response.setMessage("Course not found");
            return response;
        }

        CourseEntity courseEntity = optionalCourseEntity.get();

        courseMapper.toDTO(courseEntity);
        courseEntity.setDeleted(false);
        courseEntity.setModifiedDate(LocalDateTime.now());
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        courseEntity.setModifiedBy(authentication.getName());

        Optional<CategoryEntity> categoryEntity = categoryRepository.findById(courseDTO.getCategoryId());
        if (categoryEntity.isEmpty()) {
            response.setCode(HttpStatus.BAD_REQUEST.value());
            response.setMessage("Category not found");
            return response;
        }
        courseEntity.setCategoryEntity(categoryEntity.get());

        Optional<LanguageEntity> languageEntity = languageRepository.findById(courseDTO.getLanguageId());
        if (languageEntity.isEmpty()) {
            response.setCode(HttpStatus.BAD_REQUEST.value());
            response.setMessage("Language not found");
            return response;
        }
        courseEntity.setLanguageEntity(languageEntity.get());

        courseRepository.save(courseEntity);

        try {
            if (imageFiles != null && imageFiles.length > 0) {
                List<MultipartFile> imageFileList = Arrays.asList(imageFiles);

                if (courseEntity.getImageEntityList() != null && !courseEntity.getImageEntityList().isEmpty()) {
                    List<ImageCourseEntity> oldImageCourseEntityList = courseEntity.getImageEntityList();
                    List<Long> oldImageIds = oldImageCourseEntityList.stream()
                            .map(ImageCourseEntity::getId)
                            .collect(Collectors.toList());
                    imageCourseService.updateImages(oldImageIds, imageFileList);
                } else {
                    BaseResponse<List<ImageCourseDTO>> uploadedImagesResponse = imageCourseService.uploadFiles(imageFileList);
                    List<ImageCourseDTO> uploadedImages = uploadedImagesResponse.getData();
                    List<ImageCourseEntity> imageCourseEntities = uploadedImages.stream()
                            .map(imageSourseMapper::toEntity)
                            .collect(Collectors.toList());

                    courseEntity.setImageEntityList(imageCourseEntities);
                }
            }
            courseRepository.save(courseEntity);
        } catch (IOException e) {
            e.printStackTrace();
            response.setCode(HttpStatus.INTERNAL_SERVER_ERROR.value());
            response.setMessage("Error processing images");
            return response;
        }

        response.setCode(HttpStatus.OK.value());
        response.setMessage("Course updated successfully");
        response.setData(courseMapper.toDTO(courseEntity)); // Optionally, return updated course data
        return response;
    }




    @Override
    public BaseResponse<?> deleteCourse(Long id) {
        BaseResponse<CourseDTO> response = new BaseResponse<>();
        Optional<CourseEntity> optionalCourseEntity = courseRepository.findById(id);
        if (optionalCourseEntity.isEmpty()) {
            response.setCode(HttpStatus.BAD_REQUEST.value());
            response.setMessage(Constant.HTTP_MESSAGE.FAILED);
            return response;
        }
        CourseEntity courseEntity = optionalCourseEntity.get();
        courseEntity.setDeleted(true);
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        courseEntity.setModifiedBy(authentication.getName());
        courseEntity.setModifiedDate(LocalDateTime.now());
        courseRepository.save(courseEntity);
        CourseDTO courseDTO = courseMapper.toDTO(courseEntity);
        response.setCode(HttpStatus.OK.value());
        response.setMessage(Constant.HTTP_MESSAGE.SUCCESS);
        response.setData(courseDTO);
        return response;
    }

    @Override
    public BaseResponse<?> findById(Long id) {
        BaseResponse<CourseDTO> response = new BaseResponse<>();
        Optional<CourseEntity> optionalCourseEntity = courseRepository.findById(id);
        if (optionalCourseEntity.isEmpty()) {
            response.setCode(HttpStatus.BAD_REQUEST.value());
            response.setMessage(Constant.HTTP_MESSAGE.FAILED);
            return response;
        }

        CourseEntity courseEntity = optionalCourseEntity.get();
        if (courseEntity.getDeleted()) {
            response.setCode(HttpStatus.BAD_REQUEST.value());
            response.setMessage(Constant.HTTP_MESSAGE.FAILED);
            return response;
        }
        CourseDTO courseDTO = courseMapper.toDTO(courseEntity);
        response.setCode(HttpStatus.OK.value());
        response.setMessage(Constant.HTTP_MESSAGE.SUCCESS);
        response.setData(courseDTO);
        return response;
    }
}
