package edu.hunre.course_management.controller.resource;

import edu.hunre.course_management.model.dto.ImageDTO;
import edu.hunre.course_management.service.IImageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("/api/images")
public class ImageResource {

    @Autowired
    private IImageService fileUploadService;

    @PostMapping("/upload")
    public ResponseEntity<ImageDTO> uploadFile(@RequestParam("file") MultipartFile file) {
        try {
            ImageDTO uploadedFile = fileUploadService.uploadFile(file);
            return new ResponseEntity<>(uploadedFile, HttpStatus.OK);
        } catch (IOException e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
