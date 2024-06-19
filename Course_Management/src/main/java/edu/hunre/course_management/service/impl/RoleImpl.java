package edu.hunre.course_management.service.impl;

import edu.hunre.course_management.entity.RoleEntity;
import edu.hunre.course_management.model.dto.RoleDTO;
import edu.hunre.course_management.model.response.BaseResponse;
import edu.hunre.course_management.repository.RoleRepository;
import edu.hunre.course_management.service.IRoleService;
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
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class RoleImpl implements IRoleService {
    @Autowired
    private RoleRepository roleRepository;
    @Autowired
    private ModelMapper modelMapper;
    @Override
    public BaseResponse<Page<RoleDTO>> getAll(int page, int size) {
        Pageable pageable = PageRequest.of(page,size);
        Page<RoleEntity> pages = roleRepository.getAllRole(pageable);
        List<RoleDTO> roleDTOS = pages.getContent().stream().map(roleEntity -> {
            RoleDTO roleDTO = modelMapper.map(roleEntity,RoleDTO.class);
            return roleDTO;
        }).collect(Collectors.toList());
        BaseResponse<Page<RoleDTO>> response = new BaseResponse<>();
        response.setCode(HttpStatus.OK.value());
        response.setMessage(Constant.HTTP_MESSAGE.SUCCESS);
        response.setData(new PageImpl<>(roleDTOS, pageable, pages.getTotalElements()));
        return response;
    }

    @Override
    public BaseResponse<?> createRole(RoleDTO roleDTO) {
        BaseResponse<?> response = new BaseResponse<>();
        RoleEntity roleEntity = new RoleEntity();
        roleEntity.setName(roleDTO.getName());
        roleEntity.setDeleted(false);
        roleEntity.setCreatedDate(LocalDateTime.now());
        RoleEntity role = roleRepository.save(roleEntity);

        response.setCode(HttpStatus.OK.value());
        response.setMessage(Constant.HTTP_MESSAGE.SUCCESS);
        return response;
    }

    @Override
    public BaseResponse<RoleDTO> updateRole(Long roleId, RoleDTO roleDTO) {
        BaseResponse<RoleDTO> response = new BaseResponse<>();
        Optional<RoleEntity> optionalRoleEntity = roleRepository.findById(roleId);
        if (optionalRoleEntity.isEmpty()){
            response.setMessage(Constant.HTTP_MESSAGE.FAILED);
            response.setCode(HttpStatus.BAD_REQUEST.value());
            return response;
        }
        RoleEntity roleEntity = optionalRoleEntity.get();
        roleEntity.setName(roleDTO.getName());
        roleEntity.setDeleted(false);
        roleEntity.setModifiedDate(LocalDateTime.now());
        RoleEntity role = roleRepository.save(roleEntity);
        RoleDTO roleDTOs = modelMapper.map(role,RoleDTO.class);
        response.setData(roleDTOs);
        response.setCode(HttpStatus.OK.value());
        response.setMessage(Constant.HTTP_MESSAGE.SUCCESS);
        return response;
    }

    @Override
    public BaseResponse<RoleDTO> deleteRole(Long id) {
        BaseResponse<RoleDTO> response = new BaseResponse<>();
        Optional<RoleEntity> optionalRoleEntity = roleRepository.findById(id);
        if (optionalRoleEntity.isEmpty()){
            response.setMessage(Constant.HTTP_MESSAGE.FAILED);
            response.setCode(HttpStatus.BAD_REQUEST.value());
            return response;
        }
        RoleEntity roleEntity = optionalRoleEntity.get();
        roleEntity.setDeleted(true);
        roleEntity.setModifiedDate(LocalDateTime.now());
        roleRepository.save(roleEntity);
        RoleDTO roleDTO = modelMapper.map(roleEntity,RoleDTO.class);
        response.setCode(HttpStatus.OK.value());
        response.setData(roleDTO);
        response.setMessage(Constant.HTTP_MESSAGE.SUCCESS);
        return response;
    }

    @Override
    public BaseResponse<RoleDTO> findByIdRole(Long id) {
        BaseResponse<RoleDTO> response = new BaseResponse<>();
        Optional<RoleEntity> optionalRoleEntity = roleRepository.findById(id);
        if (optionalRoleEntity.isEmpty()){
            response.setMessage(Constant.HTTP_MESSAGE.FAILED);
            response.setCode(HttpStatus.BAD_REQUEST.value());
            return response;
        }
        RoleEntity roleEntity = optionalRoleEntity.get();
        if (roleEntity.getDeleted()){
            response.setMessage(Constant.HTTP_MESSAGE.FAILED);
            response.setCode(HttpStatus.BAD_REQUEST.value());
            return response;
        }
        RoleDTO roleDTO = modelMapper.map(roleEntity,RoleDTO.class);
        response.setCode(HttpStatus.OK.value());
        response.setMessage(Constant.HTTP_MESSAGE.SUCCESS);
        response.setData(roleDTO);
        return response;
    }
}
