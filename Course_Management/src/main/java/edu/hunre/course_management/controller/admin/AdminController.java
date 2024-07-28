package edu.hunre.course_management.controller.admin;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping(value = "/admin")
public class AdminController {
    @GetMapping(value = "/dashbroad")
    public String index(){
        return "admin/dashbroad";
    }
    @GetMapping(value = "/profile")
    public String profileAdmin(){
        return "admin/profile";
    }


}
