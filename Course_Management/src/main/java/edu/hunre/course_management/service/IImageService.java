package edu.hunre.course_management.service;
import edu.hunre.course_management.model.dto.ImageDTO;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;

public interface IImageService {
    ImageDTO uploadFile(MultipartFile file) throws IOException;
    void deleteImage(Long imageId);
    void updateImage(Long imageId, MultipartFile newFile) throws IOException;
}
