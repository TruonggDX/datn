package edu.hunre.course_management.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller

public class ImageController {
    @RequestMapping("/image")
    public String image(){
        return "admin/images";
    }
}