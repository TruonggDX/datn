package edu.hunre.course_management.service;


import edu.hunre.course_management.model.dto.CustomerDTO;
import edu.hunre.course_management.model.request.ChagePasswordRequest;
import edu.hunre.course_management.model.request.RegisterRequest;
import edu.hunre.course_management.model.response.BaseResponse;
import org.springframework.data.domain.Page;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface ICustomerService {
    CustomerDTO findUserByUsername(String username);
    BaseResponse<Page<CustomerDTO>> getAllCustomer(int page, int size);
    BaseResponse<?> addCustomer(CustomerDTO customerDTO);
    BaseResponse<?> updateCustomer(Long id, CustomerDTO customerDTO, MultipartFile imageFile);
    BaseResponse<?> deleteCustomerByID(Long customerID);
    BaseResponse<?> findCustomerByID(Long customerID);
    BaseResponse<RegisterRequest> registerCustomer(RegisterRequest registerDTO);
    BaseResponse<List<CustomerDTO>> findCustomerByUsernameAndFullname(String condition);
    BaseResponse<CustomerDTO> getUser();
    BaseResponse<?> updatePassWord(Long id, ChagePasswordRequest chagePasswordDTO);
}
