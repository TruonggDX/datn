package edu.hunre.course_management.model.dto;

import lombok.Data;

@Data
public class ImageDTO {
    private Long id;
    private String name;
    private String type;
    private byte[] file;
    private String uploadDir;
    private String base64String;
}
