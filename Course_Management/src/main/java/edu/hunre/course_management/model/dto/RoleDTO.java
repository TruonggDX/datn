package edu.hunre.course_management.model.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Getter
@Setter
public class RoleDTO {
    private Long id;
    @NotNull(message = "name role not null")
    private String name;

}
