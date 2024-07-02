package edu.hunre.course_management.controller.admin.customer;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping(value = "/admin/customer")
public class CustomerController {
    @GetMapping(value = "/list")
    public String list(){
        return "admin/customer/list_customer";
    }
}
