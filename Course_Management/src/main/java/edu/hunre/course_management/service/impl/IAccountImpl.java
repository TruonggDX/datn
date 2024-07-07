package edu.hunre.course_management.service.impl;

import edu.hunre.course_management.entity.RoleEntity;
import edu.hunre.course_management.entity.AccountEntity;
import edu.hunre.course_management.exception.ResourceNotFoundException;
import edu.hunre.course_management.model.dto.RoleDTO;
import edu.hunre.course_management.model.dto.AccountDTO;
import edu.hunre.course_management.model.response.BaseResponse;
import edu.hunre.course_management.repository.RoleRepository;
import edu.hunre.course_management.repository.AccountRepository;
import edu.hunre.course_management.service.IAccountService;
import edu.hunre.course_management.utils.Constant;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
//import org.springframework.security.core.Authentication;
//import org.springframework.security.core.context.SecurityContextHolder;
//import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class IAccountImpl implements IAccountService {
    @Autowired
    private AccountRepository accountRepository;
    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private ModelMapper modelMapper;
    @Override
    public AccountDTO findUserByUsername(String username) {
        AccountEntity accountEntity = accountRepository.findByUsername(username);
        if (accountEntity == null) {
            return null;
        }
        AccountDTO accountDto = modelMapper.map(accountEntity,AccountDTO.class);
        List<RoleDTO> roleDTOS = roleRepository.getRoleByUsername(username).stream().map(roleEntity -> modelMapper.map(roleEntity,RoleDTO.class)).toList();
        accountDto.setRoleDtos(roleDTOS);
        return accountDto;
    }

    @Override
    public BaseResponse<Page<AccountDTO>> getAllUsers(int page, int size) {
        Pageable pageable = PageRequest.of(page,size);
        Page<AccountEntity> pages = accountRepository.listUser(pageable);
        List<AccountDTO> userDTOS =pages.getContent().stream().map(accountEntity -> {
           AccountDTO userDTO = modelMapper.map(accountEntity, AccountDTO.class);
            List<Long> roleId = accountEntity.getRoles().stream().map(RoleEntity::getId).collect(Collectors.toList());
            userDTO.setRoleId(roleId);
            Set<RoleEntity> roleEntities =accountEntity.getRoles();
            List<RoleDTO> roleDTOS =roleRepository.getRoleByUsername(accountEntity.getUsername()).stream().map(roleEntity -> {
                RoleDTO roleDTO = modelMapper.map(roleEntity,RoleDTO.class);
                roleDTO.setId(roleEntity.getId());
                roleDTO.setName(roleEntity.getName());
                return roleDTO;
            }).collect(Collectors.toList());
            userDTO.setRoleDtos(roleDTOS);

            return userDTO;
        }).collect(Collectors.toList());
        BaseResponse<Page<AccountDTO>> response = new BaseResponse<>();
        response.setCode(HttpStatus.OK.value());
        response.setMessage(Constant.HTTP_MESSAGE.SUCCESS);
        response.setData(new PageImpl<>(userDTOS, pageable, pages.getTotalElements()));

        return response;
    }

    @Override
    public BaseResponse<?> createUser(AccountDTO userDTO) {
        BaseResponse<AccountDTO> response = new BaseResponse<>();

        AccountEntity accountEntity = new AccountEntity();
        accountEntity.setFullname(userDTO.getFullname());
        accountEntity.setUsername(userDTO.getUsername());
        accountEntity.setPhone(userDTO.getPhone());
        accountEntity.setAddress(userDTO.getAddress());
        accountEntity.setPassword(userDTO.getPassword());
        accountEntity.setBirthday(userDTO.getBirthday());
        accountEntity.setEmail(userDTO.getEmail());
        accountEntity.setDeleted(false);
        accountEntity.setCreatedDate(LocalDateTime.now());
        accountEntity.setCreatedBy(userDTO.getCreatedByUs());

        Set<RoleEntity> roleEntities = new HashSet<>();
        for (Long roleId : userDTO.getRoleId()) {
            RoleEntity roleEntity = roleRepository.findById(roleId).orElse(null);
            if (roleEntity != null) {
                roleEntities.add(roleEntity);
            }
        }
        accountEntity.setRoles(roleEntities);

        AccountEntity savedUser = accountRepository.save(accountEntity);
        AccountDTO accountDTO = modelMapper.map(savedUser,AccountDTO.class);

        response.setData(accountDTO);
        response.setCode(HttpStatus.OK.value());
        response.setMessage(Constant.HTTP_MESSAGE.SUCCESS);


        return response;
    }

    @Override
    public BaseResponse<AccountDTO> updateUser(Long userId, AccountDTO userDTO) {
        BaseResponse<AccountDTO> response = new BaseResponse<>();
        Optional<AccountEntity> optionalUser = accountRepository.findById(userId);
        if (optionalUser.isEmpty()){
            return new BaseResponse<>(HttpStatus.NOT_FOUND.value(), Constant.HTTP_MESSAGE.FAILED,null);
        }
        AccountEntity accountEntity = optionalUser.get();
        accountEntity.setFullname(userDTO.getFullname());
        accountEntity.setUsername(userDTO.getUsername());
        accountEntity.setPhone(userDTO.getPhone());
        accountEntity.setAddress(userDTO.getAddress());
        accountEntity.setBirthday(userDTO.getBirthday());
        accountEntity.setEmail(userDTO.getEmail());
        accountEntity.setDeleted(false);
        accountEntity.setModifiedDate(userDTO.getModifiedDate().now());
        accountEntity.setModifiedBy(userDTO.getModifiedBy());


        Set<RoleEntity> roleEntities = new HashSet<>();
        for (Long roleId : userDTO.getRoleId()) {
            RoleEntity roleEntity = roleRepository.findById(roleId).orElseThrow();
            if (roleEntity != null) {
                roleEntities.add(roleEntity);
            }
        }
        accountEntity.setRoles(roleEntities);


        AccountEntity user = accountRepository.save(accountEntity);
        AccountDTO userDTOs = modelMapper.map(user, AccountDTO.class);
        List<Long> roleIds = accountEntity.getRoles().stream().map(RoleEntity::getId).collect(Collectors.toList());
        userDTOs.setRoleId(roleIds);
        List<RoleDTO> roleDTOS = accountEntity.getRoles().stream().map(roleEntity -> {
            RoleDTO roleDTO = modelMapper.map(roleEntity, RoleDTO.class);
            roleDTO.setId(roleEntity.getId());
            roleDTO.setName(roleEntity.getName());
            return roleDTO;
        }).collect(Collectors.toList());
        userDTOs.setRoleDtos(roleDTOS);

        response.setData(userDTOs);
        response.setCode(HttpStatus.OK.value());
        response.setMessage(Constant.HTTP_MESSAGE.SUCCESS);
        return response;
    }

    @Override
    public BaseResponse<?> deletedUser(Long id) {
        BaseResponse<?> response = new BaseResponse<>();
        Optional<AccountEntity> optionalUser = accountRepository.findById(id);
        if (optionalUser.isPresent()){
            AccountEntity accountEntity = optionalUser.get();
            accountEntity.getRoles().clear();
            accountEntity.setDeleted(true);
            accountEntity.setModifiedDate(LocalDateTime.now());
            accountRepository.save(accountEntity);
            response.setCode(HttpStatus.OK.value());
            response.setMessage(Constant.HTTP_MESSAGE.SUCCESS);
        } else {
            throw new ResourceNotFoundException("Not found userId=" + id);
        }
        return response;
    }

    @Override
    public BaseResponse<AccountDTO> findAccountById(Long id) {
        BaseResponse<AccountDTO> response = new BaseResponse<>();
        Optional<AccountEntity> optionalUser = accountRepository.findById(id);
        if (optionalUser.isEmpty()){
            response.setCode(HttpStatus.BAD_REQUEST.value());
            response.setMessage(Constant.HTTP_MESSAGE.FAILED);
            return response;
        }
        AccountEntity accountEntity = optionalUser.get();
        if (accountEntity.getDeleted()){
            response.setCode(HttpStatus.BAD_REQUEST.value());
            response.setMessage(Constant.HTTP_MESSAGE.FAILED);
            return response;
        }
        AccountDTO userDTO = modelMapper.map(accountEntity, AccountDTO.class);
        List<Long> roleIds = accountEntity.getRoles().stream().map(RoleEntity::getId).collect(Collectors.toList());
        userDTO.setRoleId(roleIds);
        List<RoleDTO> roleDTOS = accountEntity.getRoles().stream().map(roleEntity -> {
            RoleDTO roleDTO = modelMapper.map(roleEntity, RoleDTO.class);
            roleDTO.setId(roleEntity.getId());
            roleDTO.setName(roleEntity.getName());
            return roleDTO;
        }).collect(Collectors.toList());
        userDTO.setRoleDtos(roleDTOS);
        response.setData(userDTO);
        response.setCode(HttpStatus.OK.value());
        response.setMessage(Constant.HTTP_MESSAGE.SUCCESS);
        return response;
    }

    @Override
    public BaseResponse<List<AccountDTO>> findUserByUsAndFn(String condition) {
        BaseResponse<List<AccountDTO>> response = new BaseResponse<>();
        List<AccountEntity> userEntities = accountRepository.findUserByUsernameAndFullname(condition);
        if (userEntities != null && !userEntities.isEmpty()){
            List<AccountDTO> userDTOS = new ArrayList<>();
            for (AccountEntity user : userEntities){
                AccountDTO userDTO = modelMapper.map(user, AccountDTO.class);
                List<Long> roleIds = user.getRoles().stream().map(RoleEntity::getId).collect(Collectors.toList());
                userDTO.setRoleId(roleIds);
                List<RoleDTO> roleDTOS = user.getRoles().stream().map(roleEntity -> {
                    RoleDTO roleDTO = modelMapper.map(roleEntity, RoleDTO.class);
                    roleDTO.setId(roleEntity.getId());
                    roleDTO.setName(roleEntity.getName());
                    return roleDTO;
                }).collect(Collectors.toList());
                userDTO.setRoleDtos(roleDTOS);
                userDTOS.add(userDTO);
            }
            response.setCode(HttpStatus.OK.value());
            response.setMessage(Constant.HTTP_MESSAGE.SUCCESS);
            response.setData(userDTOS);
        }else {
            response.setCode(HttpStatus.NOT_FOUND.value());
            response.setMessage(Constant.HTTP_MESSAGE.FAILED);
        }
        return response;
    }
//    @Override
//    public BaseResponse<AccountDTO> getUser() {
//        BaseResponse<AccountDTO> response = new BaseResponse<>();
//        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
//        if (authentication.getPrincipal() instanceof UserDetails) {
//            UserDetails userDetails = (UserDetails) authentication.getPrincipal();
//            String username = userDetails.getUsername();
//            AccountEntity accountEntity = accountRepository.findByUsername(username);
//            if (accountEntity != null) {
//                AccountDTO accountDto = modelMapper.map(accountEntity, AccountDTO.class);
//                response.setData(accountDto);
//                response.setCode(HttpStatus.OK.value());
//                response.setMessage(Constant.HTTP_MESSAGE.SUCCESS);
//                return response;
//            } else {
//                response.setCode(HttpStatus.BAD_REQUEST.value());
//                response.setMessage(Constant.HTTP_MESSAGE.FAILED);
//                return response;
//            }
//        }
//        return response;
//    }
}
