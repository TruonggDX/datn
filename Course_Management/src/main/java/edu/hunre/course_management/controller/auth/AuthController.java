//package edu.hunre.course_management.controller.auth;
//
//import edu.hunre.course_management.model.dto.CustomerDTO;
//import edu.hunre.course_management.model.dto.RoleDTO;
//import edu.hunre.course_management.model.dto.AccountDTO;
//import edu.hunre.course_management.service.IAccountService;
//import edu.hunre.course_management.service.ICustomerService;
//import edu.hunre.course_management.utils.Constant;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.security.core.Authentication;
//import org.springframework.security.core.context.SecurityContextHolder;
//import org.springframework.stereotype.Controller;
//import org.springframework.util.CollectionUtils;
//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.RequestMapping;
//
//import java.util.List;
//
//@Controller
//public class AuthController {
//    @Autowired
//    private IAccountService iAccountService;
//    @Autowired
//    private ICustomerService iCustomerService;
//
//    @RequestMapping(value = {"/","/login"})
//    public String loginPage(){
//        return "auth/login";
//    }
//
//    @GetMapping(value = "/process-after-login")
//    public String processAfterLogin() {
//        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
//        String username = authentication.getName();
//        AccountDTO accountDTO = iAccountService.findUserByUsername(username);
//        if (accountDTO != null) {
//            return handleRolesAccount(accountDTO.getRoleDtos());
//        }
//        CustomerDTO customerDTO = iCustomerService.findUserByUsername(username);
//        if (customerDTO != null) {
//            return handleRolesCustomer(customerDTO.getRoleDtos());
//        }
//        return "redirect:/logout";
//    }
//
//    private String handleRolesAccount(List<RoleDTO> roleDtos) {
//        if (CollectionUtils.isEmpty(roleDtos)) {
//            return "redirect:/logout";
//        }
//
//        boolean isAdmin = false;
//        boolean isEmployee = false;
//
//        for (RoleDTO roleDto : roleDtos) {
//            if (Constant.ROLE_ADMIN.equalsIgnoreCase(roleDto.getName())) {
//                isAdmin = true;
//            }
//            if (Constant.ROLE_EMPLOYEE.equalsIgnoreCase(roleDto.getName())) {
//                isEmployee = true;
//            }
//        }
//
//        if (isAdmin){
//            return "redirect:/admin/dashbroad";
//        }
//        if (isEmployee){
//            return "redirect:/admin/dashbroad";
//        }
//        return "redirect:/logout";
//    }
//
//    //
//    private String handleRolesCustomer(RoleDTO roleDto) {
//        if (roleDto == null) {
//            return "redirect:/logout";
//        }
//        boolean isUser = Constant.ROLE_USER.equalsIgnoreCase(roleDto.getName());
//        if (isUser){
//            return "redirect:/user/home";
//        }
//        return "redirect:/logout";
//    }
//}
