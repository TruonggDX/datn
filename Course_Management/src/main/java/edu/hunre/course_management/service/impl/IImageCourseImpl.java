package edu.hunre.course_management.service.impl;

import edu.hunre.course_management.entity.ImageCourseEntity;
import edu.hunre.course_management.exception.FileStorageException;

import edu.hunre.course_management.mapper.ImageSourseMapper;
import edu.hunre.course_management.model.dto.ImageCourseDTO;
import edu.hunre.course_management.model.response.BaseResponse;
import edu.hunre.course_management.repository.ImageCourseRepository;
import edu.hunre.course_management.service.IImageCourseService;
import edu.hunre.course_management.utils.Constant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
@Service
public class IImageCourseImpl implements IImageCourseService {

    @Autowired
    private ImageCourseRepository imageCourseRepository;

    @Autowired
    private ImageSourseMapper imageCourseMapper;


    private final Path fileStorageLocation = Paths.get("./src/main/resources/static/images_course").toAbsolutePath().normalize();


    public IImageCourseImpl() {
        try {
            Files.createDirectories(this.fileStorageLocation);
        } catch (Exception ex) {
            throw new FileStorageException("Could not create the directory for uploaded files.", ex);
        }
    }

    @Override
    public BaseResponse<List<ImageCourseDTO>> uploadFiles(List<MultipartFile> files) throws IOException {
        BaseResponse<List<ImageCourseDTO>> response = new BaseResponse<>();
        List<ImageCourseDTO> imageCourseDTOList = new ArrayList<>();

        for (MultipartFile file : files) {
            String fileName = System.currentTimeMillis() + "_" + file.getOriginalFilename(); // Ensure unique file names
            Path targetLocation = this.fileStorageLocation.resolve(fileName);
            Files.copy(file.getInputStream(), targetLocation);

            ImageCourseEntity imageCourseEntity = new ImageCourseEntity();
            imageCourseEntity.setName(fileName);
            imageCourseEntity.setType(file.getContentType());
            imageCourseEntity.setDeleted(false);
            imageCourseEntity.setCreatedDate(LocalDateTime.now());
            imageCourseEntity.setFile(file.getBytes());
            imageCourseEntity.setUploadDir(targetLocation.toString());
            imageCourseRepository.save(imageCourseEntity);

            ImageCourseDTO imageCourseDTO = imageCourseMapper.toDto(imageCourseEntity);
            imageCourseDTOList.add(imageCourseDTO);
        }

        response.setMessage(Constant.HTTP_MESSAGE.SUCCESS);
        response.setCode(HttpStatus.OK.value());
        response.setData(imageCourseDTOList);
        return response;
    }

    @Override
    public BaseResponse<?> deleteImage(Long imageId) {
        BaseResponse<?> response = new BaseResponse<>();
        Optional<ImageCourseEntity> imageCourseEntityOptional = imageCourseRepository.findById(imageId);

        if (imageCourseEntityOptional.isPresent()) {
            ImageCourseEntity imageCourseEntity = imageCourseEntityOptional.get();
            Path filePath = Paths.get(imageCourseEntity.getUploadDir());

            try {
                Files.deleteIfExists(filePath);
                imageCourseRepository.delete(imageCourseEntity);
                response.setMessage(Constant.HTTP_MESSAGE.SUCCESS);
                response.setCode(HttpStatus.OK.value());
            } catch (IOException e) {
                throw new FileStorageException("Could not delete file", e);
            }
        } else {
            response.setMessage(Constant.HTTP_MESSAGE.FAILED);
            response.setCode(HttpStatus.BAD_REQUEST.value());
        }

        return response;
    }

    @Override
    public BaseResponse<List<ImageCourseDTO>> updateImages(List<Long> imageIds, List<MultipartFile> newFiles) throws IOException {
        BaseResponse<List<ImageCourseDTO>> response = new BaseResponse<>();
        List<ImageCourseDTO> updatedImages = new ArrayList<>();

        for (int i = 0; i < imageIds.size(); i++) {
            Long imageId = imageIds.get(i);
            MultipartFile newFile = newFiles.get(i);
            Optional<ImageCourseEntity> imageCourseEntityOptional = imageCourseRepository.findById(imageId);

            if (imageCourseEntityOptional.isPresent()) {
                ImageCourseEntity imageCourseEntity = imageCourseEntityOptional.get();
                Path oldFilePath = Paths.get(imageCourseEntity.getUploadDir());

                // Đổi tên và lưu ảnh cũ
                String archiveFileName = "archive_" + System.currentTimeMillis() + "_" + oldFilePath.getFileName();
                Path archivePath = oldFilePath.resolveSibling(archiveFileName);
                Files.move(oldFilePath, archivePath);

                // Lưu ảnh mới vào thư mục
                String newFileName = System.currentTimeMillis() + "_" + newFile.getOriginalFilename(); // Ensure unique file names
                Path targetLocation = this.fileStorageLocation.resolve(newFileName);
                Files.copy(newFile.getInputStream(), targetLocation);

                // Cập nhật thông tin ảnh trong cơ sở dữ liệu
                imageCourseEntity.setName(newFileName);
                imageCourseEntity.setUploadDir(targetLocation.toString());
                imageCourseRepository.save(imageCourseEntity);

                ImageCourseDTO imageCourseDTO = imageCourseMapper.toDto(imageCourseEntity);
                updatedImages.add(imageCourseDTO);
            } else {
                response.setMessage(Constant.HTTP_MESSAGE.FAILED);
                response.setCode(HttpStatus.BAD_REQUEST.value());
                return response;
            }
        }

        response.setMessage(Constant.HTTP_MESSAGE.SUCCESS);
        response.setCode(HttpStatus.OK.value());
        response.setData(updatedImages);
        return response;
    }

}
