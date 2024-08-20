package edu.hunre.course_management.controller.user;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/user")
public class InformationController {
    @GetMapping("/information")
    public String information(){
        return "user/infor_lecturer";
    }
}
