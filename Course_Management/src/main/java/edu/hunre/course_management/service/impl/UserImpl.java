package edu.hunre.course_management.service.impl;

import edu.hunre.course_management.entity.RoleEntity;
import edu.hunre.course_management.entity.UserEntity;
import edu.hunre.course_management.exception.ResourceNotFoundException;
import edu.hunre.course_management.model.dto.RoleDTO;
import edu.hunre.course_management.model.dto.UserDTO;
import edu.hunre.course_management.model.response.BaseResponse;
import edu.hunre.course_management.repository.RoleRepository;
import edu.hunre.course_management.repository.UserRepository;
import edu.hunre.course_management.service.IUserService;
import edu.hunre.course_management.utils.Constant;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class UserImpl implements IUserService {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private ModelMapper modelMapper;
    @Override
    public UserDTO findUserByUsername(String username) {
        UserEntity userEntity = userRepository.findByUsername(username);
        UserDTO userDTO = modelMapper.map(userEntity,UserDTO.class);
        List<RoleDTO> roleDTOS = roleRepository.getRoleByUsername(username).stream().map(roleEntity -> modelMapper.map(roleEntity,RoleDTO.class)).toList();
        userDTO.setRoleDtos(roleDTOS);
        return userDTO;
    }

    @Override
    public BaseResponse<Page<UserDTO>> getAllUsers(int page, int size) {
        Pageable pageable = PageRequest.of(page,size);
        Page<UserEntity> pages = userRepository.listUser(pageable);
        List<UserDTO> userDTOS =pages.getContent().stream().map(userEntity -> {
           UserDTO userDTO = modelMapper.map(userEntity,UserDTO.class);
            List<Long> roleId = userEntity.getRoles().stream().map(RoleEntity::getId).collect(Collectors.toList());
            userDTO.setRoleId(roleId);
            Set<RoleEntity> roleEntities =userEntity.getRoles();
            List<RoleDTO> roleDTOS =roleRepository.getRoleByUsername(userEntity.getUsername()).stream().map(roleEntity -> {
                RoleDTO roleDTO = modelMapper.map(roleEntity,RoleDTO.class);
                roleDTO.setId(roleEntity.getId());
                roleDTO.setName(roleEntity.getName());
                return roleDTO;
            }).collect(Collectors.toList());
            userDTO.setRoleDtos(roleDTOS);

            return userDTO;
        }).collect(Collectors.toList());
        BaseResponse<Page<UserDTO>> response = new BaseResponse<>();
        response.setCode(HttpStatus.OK.value());
        response.setMessage(Constant.HTTP_MESSAGE.SUCCESS);
        response.setData(new PageImpl<>(userDTOS, pageable, pages.getTotalElements()));

        return response;
    }

    @Override
    public BaseResponse<?> createUser(UserDTO userDTO) {
        BaseResponse<?> response = new BaseResponse<>();

        UserEntity userEntity = new UserEntity();
        userEntity.setFullname(userDTO.getFullname());
        userEntity.setUsername(userDTO.getUsername());
        userEntity.setPhone(userDTO.getPhone());
        userEntity.setAddress(userDTO.getAddress());
        userEntity.setPassword(userDTO.getPassword());
        userEntity.setBirthday(userDTO.getBirthday());
        userEntity.setEmail(userDTO.getEmail());
        userEntity.setDeleted(false);
        userEntity.setCreatedDate(LocalDateTime.now());
        userEntity.setCreatedBy(userDTO.getCreatedByUs());

        Set<RoleEntity> roleEntities = new HashSet<>();
        for (Long roleId : userDTO.getRoleId()) {
            RoleEntity roleEntity = roleRepository.findById(roleId).orElse(null);
            if (roleEntity != null) {
                roleEntities.add(roleEntity);
            }
        }
        userEntity.setRoles(roleEntities);

        UserEntity savedUser = userRepository.save(userEntity);

        response.setCode(HttpStatus.OK.value());
        response.setMessage(Constant.HTTP_MESSAGE.SUCCESS);


        return response;
    }

    @Override
    public BaseResponse<UserDTO> updateUser(Long userId, UserDTO userDTO) {
        BaseResponse<UserDTO> response = new BaseResponse<>();
        Optional<UserEntity> optionalUser = userRepository.findById(userId);
        if (optionalUser.isEmpty()){
            return new BaseResponse<>(HttpStatus.NOT_FOUND.value(), Constant.HTTP_MESSAGE.FAILED,null);
        }
        UserEntity userEntity = optionalUser.get();
        userEntity.setFullname(userDTO.getFullname());
        userEntity.setUsername(userDTO.getUsername());
        userEntity.setPhone(userDTO.getPhone());
        userEntity.setAddress(userDTO.getAddress());
        userEntity.setBirthday(userDTO.getBirthday());
        userEntity.setEmail(userDTO.getEmail());
        userEntity.setDeleted(false);
        userEntity.setModifiedDate(userDTO.getModifiedDate().now());
        userEntity.setModifiedBy(userDTO.getModifiedBy());


        Set<RoleEntity> roleEntities = new HashSet<>();
        for (Long roleId : userDTO.getRoleId()) {
            RoleEntity roleEntity = roleRepository.findById(roleId).orElseThrow();
            if (roleEntity != null) {
                roleEntities.add(roleEntity);
            }
        }
        userEntity.setRoles(roleEntities);


        UserEntity user = userRepository.save(userEntity);
        UserDTO userDTOs = modelMapper.map(user,UserDTO.class);
        List<Long> roleIds = userEntity.getRoles().stream().map(RoleEntity::getId).collect(Collectors.toList());
        userDTOs.setRoleId(roleIds);
        List<RoleDTO> roleDTOS = userEntity.getRoles().stream().map(roleEntity -> {
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
        Optional<UserEntity> optionalUser = userRepository.findById(id);
        if (optionalUser.isPresent()){
            UserEntity userEntity = optionalUser.get();
            userEntity.getRoles().clear();
            userEntity.setDeleted(true);
            userEntity.setModifiedDate(LocalDateTime.now());
            userRepository.save(userEntity);
            response.setCode(HttpStatus.OK.value());
            response.setMessage(Constant.HTTP_MESSAGE.SUCCESS);
        } else {
            throw new ResourceNotFoundException("Not found userId=" + id);
        }
        return response;
    }

    @Override
    public BaseResponse<UserDTO> findAccountById(Long id) {
        BaseResponse<UserDTO> response = new BaseResponse<>();
        Optional<UserEntity> optionalUser = userRepository.findById(id);
        if (optionalUser.isEmpty()){
            response.setCode(HttpStatus.BAD_REQUEST.value());
            response.setMessage(Constant.HTTP_MESSAGE.FAILED);
            return response;
        }
        UserEntity userEntity = optionalUser.get();
        if (userEntity.getDeleted()){
            response.setCode(HttpStatus.BAD_REQUEST.value());
            response.setMessage(Constant.HTTP_MESSAGE.FAILED);
            return response;
        }
        UserDTO userDTO = modelMapper.map(userEntity,UserDTO.class);
        List<Long> roleIds = userEntity.getRoles().stream().map(RoleEntity::getId).collect(Collectors.toList());
        userDTO.setRoleId(roleIds);
        List<RoleDTO> roleDTOS = userEntity.getRoles().stream().map(roleEntity -> {
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
    public BaseResponse<List<UserDTO>> findUserByUsAndFn(String condition) {
        BaseResponse<List<UserDTO>> response = new BaseResponse<>();
        List<UserEntity> userEntities = userRepository.findUserByUsernameAndFullname(condition);
        if (userEntities != null && !userEntities.isEmpty()){
            List<UserDTO> userDTOS = new ArrayList<>();
            for (UserEntity user : userEntities){
                UserDTO userDTO = modelMapper.map(user,UserDTO.class);
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
}
