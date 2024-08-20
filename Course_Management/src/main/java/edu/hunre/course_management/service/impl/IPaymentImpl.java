package edu.hunre.course_management.service.impl;

import edu.hunre.course_management.entity.OrderEntity;
import edu.hunre.course_management.model.dto.CustomerDTO;
import edu.hunre.course_management.model.request.OrderFilterRequest;
import edu.hunre.course_management.model.response.BaseResponse;
import edu.hunre.course_management.repository.OrderRepository;
import edu.hunre.course_management.service.ICustomerService;
import edu.hunre.course_management.service.IPaymentService;
import edu.hunre.course_management.utils.Constant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.*;

@Service
public class IPaymentImpl implements IPaymentService {
    @Autowired
    private OrderRepository orderRepository;
    @Autowired
    private ICustomerService iCustomerService;
    @Override
    public BaseResponse<String> createVnPay(OrderFilterRequest request) {
        String orderCode = request.getCode();
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        CustomerDTO customer = iCustomerService.findUserByUsername(username);
        Optional<OrderEntity> orderEntity = orderRepository.getByCode(customer.getId(), orderCode);
        Double totalAmount = getTotalAmount(customer.getId());
        // Timestamp ngày giờ hiện tại
        Calendar cld = Calendar.getInstance(TimeZone.getTimeZone("Etc/GMT+7"));
        SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddHHmmss");

        String vnp_Version = Constant.VNP_PAY_VERSION;

        // Mã API sử dụng
        String vnp_Command = "pay";

        // Thông tin mô tả nội dung thanh toán
        String vnp_OrderInfo = "Thanh toan don hang " + orderCode;

        // Mã danh mục hàng hóa
        String orderType = "other";

        // Mã đơn hàng trên cổng thanh toán
        String vnp_TxnRef = formatter.format(cld.getTime()) + customer.getId();

        // Địa chỉ IP
        String vnp_IpAddr = "127.0.0.1";

        // Mã website của merchant trên hệ thống của VNPAY
        String vnp_TmnCode = Constant.VNP_TMN_CODE;

        // Ngôn ngữ giao diện hiển thị
        String locate = "vn";

        // Url trả về sau quá trình thanh toán
        String vnp_ReturnUrl = "http://localhost:8080/payment/result/" + orderCode;

        String vnp_CreateDate = formatter.format(cld.getTime());

        // Timestamp thêm 15p vào ngày giờ hiện tại
        cld.add(Calendar.MINUTE, 15);
        String vnp_ExpireDate = formatter.format(cld.getTime());



        Map<String, String> vnp_Params = new HashMap<>();
        vnp_Params.put("vnp_Version", vnp_Version);
        vnp_Params.put("vnp_Command", vnp_Command);
        vnp_Params.put("vnp_TmnCode", vnp_TmnCode);
        vnp_Params.put("vnp_Amount", String.valueOf(totalAmount.intValue() * 100));
        vnp_Params.put("vnp_CurrCode", "VND");
        vnp_Params.put("vnp_TxnRef", vnp_TxnRef);
        vnp_Params.put("vnp_OrderInfo", vnp_OrderInfo);
        vnp_Params.put("vnp_OrderType", orderType);
        vnp_Params.put("vnp_Locale", locate);
        vnp_Params.put("vnp_ReturnUrl", vnp_ReturnUrl);
        vnp_Params.put("vnp_IpAddr", vnp_IpAddr);
        vnp_Params.put("vnp_CreateDate", vnp_CreateDate);
        vnp_Params.put("vnp_ExpireDate", vnp_ExpireDate);

        List<String> fieldNames = new ArrayList<>(vnp_Params.keySet());
        Collections.sort(fieldNames);
        StringBuilder query = new StringBuilder();
        Iterator<String> itr = fieldNames.iterator();
        StringBuilder hashData = new StringBuilder();
        while (itr.hasNext()) {
            String fieldName = itr.next();
            String fieldValue = vnp_Params.get(fieldName);
            if ((fieldValue != null) && (!fieldValue.isEmpty())) {
                hashData.append(fieldName);
                hashData.append('=');
                hashData.append(URLEncoder.encode(fieldValue, StandardCharsets.US_ASCII));
                query.append(URLEncoder.encode(fieldName, StandardCharsets.US_ASCII));
                query.append('=');
                query.append(URLEncoder.encode(fieldValue, StandardCharsets.US_ASCII));
                if (itr.hasNext()) {
                    query.append('&');
                    hashData.append('&');
                }
            }
        }
        String queryUrl = query.toString();
        String vnp_SecureHash = hmacSHA512(Constant.VNP_HASH_SECRET, hashData.toString());
        queryUrl += "&vnp_SecureHash=" + vnp_SecureHash;
        BaseResponse<String> response = new BaseResponse<>();
        response.setCode(HttpStatus.OK.value());
        response.setMessage("Get link payment VnPay successfully");
        response.setData(Constant.VNP_PAY_URL + "?" + queryUrl);

        return response;
    }


    public static String hmacSHA512(String key, String data) {
        try {
            if (key == null || data == null) {
                throw new NullPointerException();
            }
            final Mac hmac512 = Mac.getInstance("HmacSHA512");
            byte[] hmacKeyBytes = key.getBytes();
            final SecretKeySpec secretKey = new SecretKeySpec(hmacKeyBytes, "HmacSHA512");
            hmac512.init(secretKey);
            byte[] dataBytes = data.getBytes(StandardCharsets.UTF_8);
            byte[] result = hmac512.doFinal(dataBytes);
            StringBuilder sb = new StringBuilder(2 * result.length);
            for (byte b : result) {
                sb.append(String.format("%02x", b & 0xff));
            }
            return sb.toString();
        } catch (Exception ex) {
            return "";
        }
    }
    public Double getTotalAmount(Long customerId) {
        return orderRepository.calculateTotalAmountByCustomerId(customerId);
    }

}
