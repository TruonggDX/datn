package edu.hunre.course_management.controller.admin;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping(value = "/admin/language")
public class LanguageController {
    @GetMapping(value = "/list")
    public String list(){
        return "admin/language/list_language";
    }
}
