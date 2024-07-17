package edu.hunre.course_management.model.request;

import lombok.Data;

@Data
public class ChagePasswordRequest {
    private Long id;
    private String oldPassword;
    private String newPassword;
    private String confirmPassword;
}
