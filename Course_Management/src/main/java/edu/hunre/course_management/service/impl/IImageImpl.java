package edu.hunre.course_management.service.impl;

import edu.hunre.course_management.entity.ImageEntity;
import edu.hunre.course_management.exception.FileStorageException;
import edu.hunre.course_management.model.dto.ImageDTO;
import edu.hunre.course_management.repository.ImageRepository;
import edu.hunre.course_management.service.IImageService;
import jakarta.transaction.Transactional;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;
import org.apache.tomcat.util.codec.binary.Base64;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.LocalDateTime;
import java.util.Objects;

@Service
public class IImageImpl implements IImageService {
    private final ImageRepository imageRepository;
    private Path uploadLocation;
    @Autowired
    private ModelMapper modelMapper;

    public IImageImpl(ImageRepository imageRepository, ImageEntity fileUpload) {
        this.imageRepository = imageRepository;
        this.uploadLocation= Paths.get(fileUpload.getUploadDir()).toAbsolutePath().normalize();
        try {
            Files.createDirectories(this.uploadLocation);
        }catch (Exception ex){
            throw new FileStorageException("counld not create directory",ex);

        }
    }

    public ImageDTO uploadFile(MultipartFile file) throws IOException {
        String originalFilename = StringUtils.cleanPath(Objects.requireNonNull(file.getOriginalFilename()));
        Path targetLocation = this.uploadLocation.resolve(originalFilename);
        Files.copy(file.getInputStream(), targetLocation, StandardCopyOption.REPLACE_EXISTING);
        ImageEntity fileUpload = new ImageEntity();
        fileUpload.setUploadDir(String.valueOf(this.uploadLocation));
        fileUpload.setType(file.getContentType());
        fileUpload.setDeleted(false);
        fileUpload.setCreatedDate(LocalDateTime.now());
        fileUpload.setName(originalFilename);
        fileUpload.setFile(file.getBytes());
        ImageEntity savedEntity = imageRepository.save(fileUpload);

        ImageDTO imageDTO = modelMapper.map(savedEntity, ImageDTO.class);
        return imageDTO;
    }

    @Override
    @Transactional
    public void deleteImage(Long imageId) {
        if (imageRepository.existsById(imageId)) {
            ImageEntity imageEntity = imageRepository.findById(imageId).orElse(null);
            if (imageEntity != null) {
                imageEntity.setDeleted(false);
                imageEntity.setModifiedDate(LocalDateTime.now());
                imageRepository.delete(imageEntity);
            }

        } else {
            throw new RuntimeException("Không tìm thấy ảnh với id: " + imageId);
        }
    }

    @Override
    @Transactional
    public void updateImage(Long imageId, MultipartFile newFile) throws IOException {
        // Tìm kiếm entity ảnh cần cập nhật
        ImageEntity imageEntity = imageRepository.findById(imageId)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy ảnh với ID: " + imageId));

        // Xóa file ảnh cũ từ hệ thống nếu tồn tại
        if (imageEntity.getName() != null) {
            Path oldPath = Paths.get(imageEntity.getName());
            Files.deleteIfExists(oldPath);
        }

        // Upload file ảnh mới và lưu thông tin vào entity ảnh
        String originalFilename = StringUtils.cleanPath(Objects.requireNonNull(newFile.getOriginalFilename()));
        Path targetLocation = this.uploadLocation.resolve(originalFilename);
        Files.copy(newFile.getInputStream(), targetLocation, StandardCopyOption.REPLACE_EXISTING);

        // Cập nhật thông tin của ảnh trong entity
        imageEntity.setName(originalFilename);
        imageEntity.setType(newFile.getContentType());
        imageEntity.setUploadDir(targetLocation.toString());  // Lưu đường dẫn mới của file
        imageEntity.setFile(newFile.getBytes());
        imageEntity.setModifiedDate(LocalDateTime.now());
        imageEntity.setDeleted(false);

        // Lưu lại entity đã cập nhật vào cơ sở dữ liệu
        imageRepository.save(imageEntity);
    }


}
