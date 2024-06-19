package edu.hunre.course_management.controller.user;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping(value = "/user")
public class UserController {
    @GetMapping(value = "/home")
    public String home(){
        return "user/home";
    }
    @GetMapping(value = "/profile")
    public String profile(){
        return "user/profile";
    }
}
