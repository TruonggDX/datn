package edu.hunre.course_management.controller.user;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/course")
public class DetailCourseController {
    @GetMapping("/detail_course")
    public String detail_course(){
        return "user/detail_course";
    }



}
