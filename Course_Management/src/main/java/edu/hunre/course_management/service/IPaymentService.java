package edu.hunre.course_management.service;

import edu.hunre.course_management.model.request.OrderFilterRequest;
import edu.hunre.course_management.model.response.BaseResponse;

public interface IPaymentService {
    BaseResponse<String> createVnPay(OrderFilterRequest request);
}
