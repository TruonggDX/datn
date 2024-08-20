package edu.hunre.course_management.controller.resource;

import edu.hunre.course_management.model.request.OrderFilterRequest;
import edu.hunre.course_management.service.IPaymentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/payment")
public class PaymentResource {
    @Autowired
    private IPaymentService iPaymentService;
    @PostMapping("/create_vnpay")
    public ResponseEntity<?> getVnPay(@RequestBody OrderFilterRequest request) {
        return ResponseEntity.ok(iPaymentService.createVnPay(request));
    }
}
