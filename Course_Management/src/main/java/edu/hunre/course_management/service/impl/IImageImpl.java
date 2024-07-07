package edu.hunre.course_management.service.impl;

import edu.hunre.course_management.entity.ImageEntity;
import edu.hunre.course_management.exception.FileStorageException;
import edu.hunre.course_management.model.dto.ImageDTO;
import edu.hunre.course_management.repository.ImageRepository;
import edu.hunre.course_management.service.IImageService;
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

}
