//package edu.hunre.course_management.controller.auth;
//
//import edu.hunre.course_management.model.dto.RoleDTO;
//import edu.hunre.course_management.model.dto.UserDTO;
//import edu.hunre.course_management.service.IUserService;
//import edu.hunre.course_management.utils.Constant;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.security.core.Authentication;
//import org.springframework.security.core.context.SecurityContextHolder;
//import org.springframework.stereotype.Controller;
//import org.springframework.ui.Model;
//import org.springframework.util.CollectionUtils;
//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.RequestMapping;
//
//@Controller
//public class AuthController {
//    @Autowired
//    private IUserService iUserService;
//    @RequestMapping(value = {"/","/login"})
//    public String loginPage(){
//        return "auth/login";
//    }
//
//
//    @GetMapping(value = "/process-after-login")
//    public String processAfterLogin() {
//        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
//        String username = authentication.getName();
//        UserDTO userDto= iUserService.findUserByUsername(username);
//        if (CollectionUtils.isEmpty(userDto.getRoleDtos())){
//            return "redirect:/logout";
//        }
//        boolean isAdmin = false;
//        boolean isUser = false;
//        boolean isEmployee = false;
//        for (int i = 0; i < userDto.getRoleDtos().size(); i++) {
//            RoleDTO roleDto = userDto.getRoleDtos().get(i);
//            if (Constant.ROLE_ADMIN.equalsIgnoreCase(roleDto.getName())){
//                isAdmin=true;
//            }
//            if (Constant.ROLE_USER.equalsIgnoreCase(roleDto.getName())){
//                isUser=true;
//            }
//            if (Constant.ROLE_EMPLOYEE.equalsIgnoreCase(roleDto.getName())){
//                isEmployee=true;
//            }
//        }
//        if (isAdmin){
//            return "redirect:/admin/dashbroad";
//        }
//        if (isEmployee){
//            return "redirect:/employee/dashbroad";
//        }
//        if (isUser){
//            return "redirect:/user/home";
//        }
//        return "redirect:/logout";
//    }
//}
