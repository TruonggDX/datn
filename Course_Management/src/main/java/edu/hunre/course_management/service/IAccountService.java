package edu.hunre.course_management.service;

import edu.hunre.course_management.model.dto.AccountDTO;
import edu.hunre.course_management.model.request.AccountRequest;
import edu.hunre.course_management.model.request.ChagePasswordRequest;
import edu.hunre.course_management.model.response.BaseResponse;
import org.springframework.data.domain.Page;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface IAccountService {
    AccountDTO findUserByUsername(String username);

    BaseResponse<Page<AccountDTO>> getAllUsers(int page , int size);
    BaseResponse<?> createUser(AccountDTO userDTO);
    BaseResponse<AccountDTO> updateUser(Long userId, AccountDTO userDTO, MultipartFile imageFile);
    BaseResponse<?> deletedUser(Long id);
    BaseResponse<AccountDTO> findAccountById(Long id);
    BaseResponse<AccountDTO> getUser();
    BaseResponse<List<AccountDTO>> findUserByUsAndFn(String condition);
    BaseResponse<?> updatePassWord(Long id, ChagePasswordRequest chagePasswordDTO);
    BaseResponse<?> updateBatch(Long id,AccountDTO accountDTO, MultipartFile multipartFile);
}
