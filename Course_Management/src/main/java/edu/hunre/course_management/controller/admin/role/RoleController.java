package edu.hunre.course_management.controller.admin.role;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/admin/role")
public class RoleController {
    @GetMapping(value = "/list")
    public String listRole(){
        return "admin/role/list_role";
    }
}
