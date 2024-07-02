package edu.hunre.course_management.model.dto;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Getter
@Setter
public class CustomerDTO {
    private Long id;
    private String fullname;
    private String username;
    private String phone;
    private String address;
    private String password;
    private String certificate;
    private Long roleId;
    RoleDTO roleDtos;
}
