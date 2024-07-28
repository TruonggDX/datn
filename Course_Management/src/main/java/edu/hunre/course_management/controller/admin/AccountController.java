package edu.hunre.course_management.controller.admin;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping(value = "/admin/account")
public class AccountController {
    @GetMapping(value = "/list")
    public String listAccount(){
        return "admin/account/list_account";
    }
}
