package edu.hunre.course_management.service;

import edu.hunre.course_management.model.dto.CustomerDTO;
import edu.hunre.course_management.model.request.RegisterDTO;
import edu.hunre.course_management.model.response.BaseResponse;
import org.springframework.data.domain.Page;

import java.util.List;

public interface ICustomerService {
    CustomerDTO findUserByUsername(String username);
    BaseResponse<Page<CustomerDTO>> getAllCustomer(int page, int size);
    BaseResponse<?> addCustomer(CustomerDTO customerDTO);
    BaseResponse<?> updateCustomer(Long id,CustomerDTO customerDTO);
    BaseResponse<?> deleteCustomerByID(Long customerID);
    BaseResponse<?> findCustomerByID(Long customerID);
    BaseResponse<RegisterDTO> registerCustomer(RegisterDTO registerDTO);
    BaseResponse<List<CustomerDTO>> findCustomerByUsernameAndFullname(String condition);
}
