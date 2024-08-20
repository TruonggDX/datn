package edu.hunre.course_management.model.request;

import lombok.Data;

@Data
public class AccountRequest {
    private Long idAccount;
    private String username;
    private String fullname;
    private String description;
    private String title;
    private String image;
}
