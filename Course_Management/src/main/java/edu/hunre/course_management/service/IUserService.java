package edu.hunre.course_management.service;

import edu.hunre.course_management.model.dto.UserDTO;
import edu.hunre.course_management.model.response.BaseResponse;
import org.springframework.data.domain.Page;

import java.util.List;

public interface IUserService {
    UserDTO findUserByUsername(String username);

    BaseResponse<Page<UserDTO>> getAllUsers(int page , int size);
    BaseResponse<?> createUser(UserDTO userDTO);
    BaseResponse<UserDTO> updateUser(Long userId,UserDTO userDTO);
    BaseResponse<?> deletedUser(Long id);
    BaseResponse<UserDTO> findAccountById(Long id);

    BaseResponse<List<UserDTO>> findUserByUsAndFn(String condition);
}
