package edu.hunre.course_management.model.request;

import lombok.Data;

@Data
public class ContentCourseRequest {
    private String name;
    private String content;
    private String link;
    private Integer duration;
    private Integer number;
}
