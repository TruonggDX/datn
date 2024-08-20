package edu.hunre.course_management.controller.admin;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/admin/course")
public class CourseController {
    @GetMapping("/list")
    public String list(){
        return "admin/course/list_course";
    }
    @GetMapping("/detailsCourse")
    public String detailsCourse(){
        return "admin/course/details_course";
    }


}
