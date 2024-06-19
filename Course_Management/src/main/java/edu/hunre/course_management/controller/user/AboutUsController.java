package edu.hunre.course_management.controller.user;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class AboutUsController {
    @RequestMapping(value = "/introduce")
    public String introduce(){
        return "user/aboutUs/introduce";
    }
    @RequestMapping(value = "/contact")
    public String contacts(){
        return "user/aboutUs/contact";
    }
}
