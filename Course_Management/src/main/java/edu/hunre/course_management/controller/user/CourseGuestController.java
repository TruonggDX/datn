package edu.hunre.course_management.controller.user;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/course")
public class CourseGuestController {
    @GetMapping("list")
    public String list(){
        return "user/list_course";
    }
    @GetMapping("/my-courses/wishlist")
    public String wishlist(){
        return "user/wish_list";
    }
}
