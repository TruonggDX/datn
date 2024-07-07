package edu.hunre.course_management.service;

import edu.hunre.course_management.model.dto.AccountDTO;
import edu.hunre.course_management.model.response.BaseResponse;
import org.springframework.data.domain.Page;

import java.util.List;

public interface IAccountService {
    AccountDTO findUserByUsername(String username);

    BaseResponse<Page<AccountDTO>> getAllUsers(int page , int size);
    BaseResponse<?> createUser(AccountDTO userDTO);
    BaseResponse<AccountDTO> updateUser(Long userId, AccountDTO userDTO);
    BaseResponse<?> deletedUser(Long id);
    BaseResponse<AccountDTO> findAccountById(Long id);
//    BaseResponse<AccountDTO> getUser();
    BaseResponse<List<AccountDTO>> findUserByUsAndFn(String condition);

}
