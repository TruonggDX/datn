package edu.hunre.course_management.model.request;

import edu.hunre.course_management.model.dto.RoleDTO;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RegisterRequest {
    private Long id;
    private String username;
    private String password;
    private RoleDTO roleDtos;
}
