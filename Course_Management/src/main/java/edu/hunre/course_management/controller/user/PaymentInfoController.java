package edu.hunre.course_management.controller.user;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/payment")
public class PaymentInfoController {
    @GetMapping("/info")
    public String info(){
        return "user/payment_info";
    }

    @GetMapping("/result/{code}")
    public String resultPayment(@PathVariable String code) {
        return "user/result_payment";
    }

}
