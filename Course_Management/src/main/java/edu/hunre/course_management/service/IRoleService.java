package edu.hunre.course_management.service;

import edu.hunre.course_management.model.dto.RoleDTO;
import edu.hunre.course_management.model.response.BaseResponse;
import org.springframework.data.domain.Page;

import javax.management.relation.Role;

public interface IRoleService {
    BaseResponse<Page<RoleDTO>> getAll(int page,int size);
    BaseResponse<?> createRole (RoleDTO roleDTO);
    BaseResponse<RoleDTO> updateRole(Long roleId,RoleDTO roleDTO);
    BaseResponse<RoleDTO> deleteRole(Long id);
    BaseResponse<RoleDTO> findByIdRole(Long id);
}
