package edu.hunre.course_management.service.impl;

import edu.hunre.course_management.entity.*;
import edu.hunre.course_management.mapper.CourseMapper;
import edu.hunre.course_management.mapper.ImageSourseMapper;
import edu.hunre.course_management.model.dto.*;
import edu.hunre.course_management.model.request.CourseFilterRequest;
import edu.hunre.course_management.model.response.BaseResponse;
import edu.hunre.course_management.repository.*;
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
    @Autowired
    private AccountRepository accountRepository;
    @Autowired
    private LevelRepository levelRepository;

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
        Optional<AccountEntity> account = accountRepository.findById(courseDTO.getAccountId());
        if (account.isEmpty()) {
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
        courseEntity.setAccountEntity(account.get());
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
        
        courseDTO = courseMapper.toDtoCustom(courseEntity);
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

        Optional<AccountEntity> accountEntity = accountRepository.findById(courseDTO.getAccountId());
        if (accountEntity.isEmpty()) {
            response.setCode(HttpStatus.BAD_REQUEST.value());
            response.setMessage("account not found");
            return response;
        }
        courseEntity.setAccountEntity(accountEntity.get());
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
        response.setData(courseMapper.toDTO(courseEntity));
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

    @Override
    public BaseResponse<List<CourseDTO>> findCourseByName(String name) {
        BaseResponse<List<CourseDTO>> response = new BaseResponse<>();
        List<CourseEntity> courseEntity = courseRepository.findCourseByName(name);
        if (courseEntity == null && courseEntity.isEmpty()){
            response.setCode(HttpStatus.NOT_FOUND.value());
            response.setMessage(Constant.HTTP_MESSAGE.FAILED);
            response.setData(new ArrayList<>());
        }

        List<CourseDTO> courseDTO = new ArrayList<>();
        for (CourseEntity courseEntitys : courseEntity) {
            courseDTO.add(courseMapper.toDTO(courseEntitys));
        }
        response.setCode(HttpStatus.OK.value());
        response.setMessage(Constant.HTTP_MESSAGE.SUCCESS);
        response.setData(courseDTO);

        return response;
    }

    @Override
    public BaseResponse<Page<CourseDTO>> findCourseByCategoryId(Map<String, String> params,int page,int size) {
       String categoryIdStr = params.get("categoryId");
       String levelIdStr = params.get("levelId");
       String languageIdStr = params.get("languageId");
        Long categoryId = null;
        Long levelId = null;
        Long languageId=null;
//        Optional<CategoryEntity> optionalCategoryEntity = categoryRepository.findById(categoryId);
//        if (optionalCategoryEntity.isEmpty()) {
//            return new BaseResponse<>(HttpStatus.BAD_REQUEST.value(), Constant.HTTP_MESSAGE.FAILED,null);
//        }
        Page<CourseEntity> courseEntityPage = null;
        Pageable pageable = PageRequest.of(page, size);
        if(!categoryIdStr.isEmpty()){
             categoryId = Long.valueOf(categoryIdStr);
        }
        if (!levelIdStr.isEmpty()) {
             levelId = Long.valueOf(levelIdStr);
        }
        if (!languageIdStr.isEmpty()) {
            languageId = Long.valueOf(languageIdStr);
        }
        courseEntityPage = courseRepository.findCourseByCategoryIdAndLevelId(categoryId, pageable, levelId,languageId);


        List<CourseDTO> courseDTO = new ArrayList<>();
        for (CourseEntity courseEntity : courseEntityPage) {
            courseDTO.add(courseMapper.toDTO(courseEntity));
        }
        BaseResponse<Page<CourseDTO>> response = new BaseResponse<>();
        response.setCode(HttpStatus.OK.value());
        response.setMessage(Constant.HTTP_MESSAGE.SUCCESS);
        response.setData(new PageImpl<>(courseDTO, pageable, courseEntityPage.getTotalElements()));
        return response;
    }

    @Override
    public BaseResponse<List<LevelCourseCountDTO>> countCoursesByLevel(List<Long> levelIds) {
        BaseResponse<List<LevelCourseCountDTO>> response = new BaseResponse<>();
        try {
            List<Object[]> courseCounts = courseRepository.countCoursesByLevel(levelIds);
            List<LevelCourseCountDTO> levelCourseCounts = new ArrayList<>();

            for (Object[] obj : courseCounts) {
                Long levelId = (Long) obj[0];
                Long count = (Long) obj[1];
                levelCourseCounts.add(new LevelCourseCountDTO(levelId, count));
            }

            response.setData(levelCourseCounts);
            response.setMessage(Constant.HTTP_MESSAGE.SUCCESS);
            response.setCode(HttpStatus.OK.value());
        } catch (Exception e) {
            response.setMessage(e.getMessage());
            response.setCode(HttpStatus.INTERNAL_SERVER_ERROR.value());
        }
        return response;
    }

    @Override
    public BaseResponse<Page<CourseDTO>> getAllCourseWithList(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<CourseEntity> courseEntityPage = courseRepository.findAllCourseByWishList(pageable);
        List<CourseDTO> courseDTO = new ArrayList<>();
        for (CourseEntity courseEntity : courseEntityPage) {
            courseDTO.add(courseMapper.toDTO(courseEntity));
        }
        BaseResponse<Page<CourseDTO>> response = new BaseResponse<>();
        response.setCode(HttpStatus.OK.value());
        response.setMessage(Constant.HTTP_MESSAGE.SUCCESS);
        response.setData(new PageImpl<>(courseDTO, pageable, courseEntityPage.getTotalElements()));
        return response;
    }

    @Override
    public BaseResponse<Page<CourseDTO>> getAllCourseByAccountId(Long accountId, int page, int size) {
        Optional<AccountEntity> optionalAccountEntity = accountRepository.findById(accountId);
        if (optionalAccountEntity.isEmpty()) {
            return new BaseResponse<>(HttpStatus.BAD_REQUEST.value(),Constant.HTTP_MESSAGE.FAILED,null);
        }
        Pageable pageable = PageRequest.of(page, size);
        Page<CourseEntity> courseEntityPage = courseRepository.findCourseByAccountId(accountId, pageable);
        List<CourseDTO> courseDTO = new ArrayList<>();
        for (CourseEntity courseEntity : courseEntityPage) {
            courseDTO.add(courseMapper.toDTO(courseEntity));
        }
        BaseResponse<Page<CourseDTO>> response = new BaseResponse<>();
        response.setCode(HttpStatus.OK.value());
        response.setMessage(Constant.HTTP_MESSAGE.SUCCESS);
        response.setData(new PageImpl<>(courseDTO, pageable, courseEntityPage.getTotalElements()));
        return response;
    }

    @Override
    public BaseResponse<Long> countCourseByAccountId(Long accountId) {
        BaseResponse<Long> response = new BaseResponse<>();
        Optional<AccountEntity> optionalAccountEntity = accountRepository.findById(accountId);
        if (optionalAccountEntity.isEmpty()) {
            response.setCode(HttpStatus.NOT_FOUND.value());
            response.setMessage(Constant.HTTP_MESSAGE.FAILED);
            return response;
        }
        Long count = courseRepository.countCourseByAccountId(accountId);
        response.setCode(HttpStatus.OK.value());
        response.setMessage(Constant.HTTP_MESSAGE.SUCCESS);
        response.setData(count);
        return response;
    }

    @Override
    public BaseResponse<Page<CourseDTO>> getCourseByLevelId(Long levelId, int page, int size) {
        Optional<LevelEntity> levelEntity = levelRepository.findById(levelId);
        if (levelEntity.isEmpty()) {
            return new BaseResponse<>(HttpStatus.BAD_REQUEST.value(),Constant.HTTP_MESSAGE.FAILED,null);
        }
        Pageable pageable = PageRequest.of(page, size);
        Page<CourseEntity> courseEntityPage = courseRepository.findCourseByLevelId(levelId, pageable);
        List<CourseDTO> courseDTO = new ArrayList<>();
        for (CourseEntity courseEntity : courseEntityPage) {
            courseDTO.add(courseMapper.toDTO(courseEntity));
        }
        BaseResponse<Page<CourseDTO>> response = new BaseResponse<>();
        response.setCode(HttpStatus.OK.value());
        response.setMessage(Constant.HTTP_MESSAGE.SUCCESS);
        response.setData(new PageImpl<>(courseDTO, pageable, courseEntityPage.getTotalElements()));
        return response;
    }




}
