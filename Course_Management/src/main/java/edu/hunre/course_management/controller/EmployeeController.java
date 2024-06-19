package edu.hunre.course_management.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/employee")
public class EmployeeController {
    @GetMapping(value = "/dashbroad")
    public String index(){
        return "admin/dashbroad";
    }

    @GetMapping(value = "/list")
    public String listAccount(){
        return "admin/account/list_account";
    }
}
