package edu.hunre.course_management.exception;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;
@Data
@Getter
@Setter
public class ErrorResponse {
    private Date timestamp;
    private Integer status;
    private String path;
    private String error;
    private String message;

}
