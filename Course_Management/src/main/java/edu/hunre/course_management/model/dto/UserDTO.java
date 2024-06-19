package edu.hunre.course_management.model.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import edu.hunre.course_management.model.validator.PhoneNumber;
import edu.hunre.course_management.model.validator.RoleValid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
@Data
public class UserDTO {

    private Long id;

    @NotNull(message = "fullname not null")
    private String fullname;

    @NotEmpty(message = "username not null")
    private String username;

    @PhoneNumber(message = "phone invalid format")
    private String phone;

    @NotEmpty(message = "address not null")
    private String address;

    @NotEmpty(message = "password not null")
    private String password;

    @NotNull(message = "dateOfBirth must be not null")
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    @JsonFormat(pattern = "MM/dd/yyyy")
    private Date birthday;

    @Email(message = "email invalid format")
    private String email;


    @RoleValid(message = "role not null")
    private List<Long> roleId;

    private String createdByUs;
    private LocalDateTime createdTime;
    private String modifiedBy;
    private LocalDateTime modifiedDate;
    List<RoleDTO> roleDtos;
}
