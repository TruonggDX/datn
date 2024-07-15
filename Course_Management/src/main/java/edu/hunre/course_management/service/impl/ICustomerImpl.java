package edu.hunre.course_management.service.impl;

import edu.hunre.course_management.entity.AccountEntity;
import edu.hunre.course_management.entity.CustomerEntity;
import edu.hunre.course_management.entity.ImageEntity;
import edu.hunre.course_management.entity.RoleEntity;
import edu.hunre.course_management.model.dto.CustomerDTO;
import edu.hunre.course_management.model.dto.CustomerDTO;
import edu.hunre.course_management.model.dto.ImageDTO;
import edu.hunre.course_management.model.dto.RoleDTO;
import edu.hunre.course_management.model.request.ChagePasswordDTO;
import edu.hunre.course_management.model.request.RegisterDTO;
import edu.hunre.course_management.model.response.BaseResponse;
import edu.hunre.course_management.repository.CustomerRepository;
import edu.hunre.course_management.repository.ImageRepository;
import edu.hunre.course_management.repository.RoleRepository;
import edu.hunre.course_management.service.ICustomerService;
import edu.hunre.course_management.service.IImageService;
import edu.hunre.course_management.utils.Constant;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ICustomerImpl implements ICustomerService {
    @Autowired
    private CustomerRepository customerRepository;
    @Autowired
    private RoleRepository roleRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private ModelMapper modelMapper;
    @Autowired
    private IImageService imageService;
    @Autowired
    private ImageRepository imageRepository;
    @Override
    public CustomerDTO findUserByUsername(String username) {
        CustomerEntity customerEntity = customerRepository.findByUsername(username);
        if (customerEntity == null) {
            return null;
        }

        CustomerDTO customerDto = modelMapper.map(customerEntity, CustomerDTO.class);
        RoleEntity roleEntity = roleRepository.findByRoleCustomer(username);
        RoleDTO roleDTO = modelMapper.map(roleEntity, RoleDTO.class);
        customerDto.setRoleDtos(roleDTO);
        return customerDto;
    }

    @Override
    public BaseResponse<Page<CustomerDTO>> getAllCustomer(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<CustomerEntity> customerEntities = customerRepository.findAll(pageable);
        List<CustomerDTO> customerDTOList = customerEntities.getContent().stream().map(customerEntity -> {
            CustomerDTO customerDTO = modelMapper.map(customerEntity, CustomerDTO.class);
            customerDTO.setRoleId(customerEntity.getRoleEntity().getId());
            RoleEntity roleEntities =customerEntity.getRoleEntity();
            RoleDTO roleDTO = modelMapper.map(roleEntities, RoleDTO.class);
            roleDTO.setId(roleEntities.getId());
            roleDTO.setName(roleEntities.getName());
            customerDTO.setRoleDtos(roleDTO);

            ImageEntity imageEntity = customerEntity.getImageEntity();
            if (imageEntity != null) {
                try {
                    byte[] fileBytes = imageEntity.getFile();
                    String base64Image = Base64.getEncoder().encodeToString(fileBytes);
                    customerDTO.setImage(base64Image);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            return customerDTO;
        }).collect(Collectors.toList());
        BaseResponse<Page<CustomerDTO>> response = new BaseResponse<>();
        response.setCode(HttpStatus.OK.value());
        response.setMessage(Constant.HTTP_MESSAGE.SUCCESS);
        response.setData(new PageImpl<>(customerDTOList, pageable, customerEntities.getTotalElements()));

        return response;
    }

    @Override
    public BaseResponse<?> addCustomer(CustomerDTO customerDTO) {
        BaseResponse<CustomerDTO> response = new BaseResponse<>();
        CustomerEntity customerEntity = new CustomerEntity();
        customerEntity.setFullname(customerDTO.getFullname());
        customerEntity.setUsername(customerDTO.getUsername());
        customerEntity.setPhone(customerDTO.getPhone());
        customerEntity.setPassword(customerDTO.getPassword());
        customerEntity.setAddress(customerDTO.getAddress());
        customerEntity.setEmail(customerDTO.getEmail());
        customerEntity.setPassword(passwordEncoder.encode(customerDTO.getPassword()));
        customerEntity.setDeleted(false);

        RoleEntity roleEntity = roleRepository.findById(2L).orElseGet(() -> {
            RoleEntity newRole = new RoleEntity();
            newRole.setId(2L);
            newRole.setName(Constant.ROLE_USER);
            return roleRepository.save(newRole);
        });
        customerEntity.setRoleEntity(roleEntity);

        customerRepository.save(customerEntity);
        CustomerDTO customerDTOs = modelMapper.map(customerEntity, CustomerDTO.class);
        customerDTOs.setId(customerEntity.getId());
        RoleEntity roleEntities = customerEntity.getRoleEntity();
        RoleDTO roleDTO = modelMapper.map(roleEntities, RoleDTO.class);
        roleDTO.setId(roleEntities.getId());
        roleDTO.setName(roleEntities.getName());
        customerDTOs.setRoleDtos(roleDTO);
        response.setCode(HttpStatus.OK.value());
        response.setMessage(Constant.HTTP_MESSAGE.SUCCESS);
        response.setData(customerDTOs);

        return response;
    }

    @Override
    public BaseResponse<CustomerDTO> updateCustomer(Long id, CustomerDTO customerDTO, MultipartFile imageFile) {
        BaseResponse<CustomerDTO> response = new BaseResponse<>();
        Optional<CustomerEntity> customerEntityOptional = customerRepository.findById(id);

        if (customerEntityOptional.isEmpty()) {
            response.setMessage(Constant.HTTP_MESSAGE.FAILED);
            response.setCode(HttpStatus.NOT_FOUND.value());
            return response;
        }

        CustomerEntity customer = customerEntityOptional.get();
//        if (customerRepository.existsByUsername(customerDTO.getUsername())) {
//            response.setCode(HttpStatus.BAD_REQUEST.value());
//            response.setMessage(Constant.HTTP_MESSAGE.FAILED);
//            return response;
//        }
        customer.setUsername(customerDTO.getUsername());
        customer.setFullname(customerDTO.getFullname());
        customer.setPhone(customerDTO.getPhone());
        customer.setEmail(customerDTO.getEmail());
        customer.setAddress(customerDTO.getAddress());

        try {
            if (imageFile != null && !imageFile.isEmpty()) {
                // Kiểm tra xem đã có ảnh liên kết với khách hàng hay chưa
                if (customer.getImageEntity() != null) {
                    // Đã có ảnh, cập nhật lại
                    ImageEntity oldImageEntity = customer.getImageEntity();
                    imageService.updateImage(oldImageEntity.getId(), imageFile);
                } else {
                    // Chưa có ảnh, thêm mới
                    ImageDTO uploadedImage = imageService.uploadFile(imageFile);
                    ImageEntity imageEntity = modelMapper.map(uploadedImage, ImageEntity.class);
                    imageEntity.setCustomer(customer);
                    imageEntity.setDeleted(false);
                    imageEntity.setCreatedDate(LocalDateTime.now());
                    imageRepository.save(imageEntity);
                    customer.setImageEntity(imageEntity);
                }
            }

            customerRepository.save(customer);

            // Map customer entity back to DTO
            CustomerDTO updatedCustomerDTO = modelMapper.map(customer, CustomerDTO.class);
            RoleEntity roleEntity = customer.getRoleEntity();
            RoleDTO roleDTO = modelMapper.map(roleEntity, RoleDTO.class);
            roleDTO.setId(roleEntity.getId());
            roleDTO.setName(roleEntity.getName());
            updatedCustomerDTO.setRoleDtos(roleDTO);
            response.setMessage(Constant.HTTP_MESSAGE.SUCCESS);
            response.setCode(HttpStatus.OK.value());
            response.setData(updatedCustomerDTO);
            return response;

        } catch (IOException e) {
            response.setCode(HttpStatus.INTERNAL_SERVER_ERROR.value());
            response.setMessage(Constant.HTTP_MESSAGE.FAILED);
            return response;
        }
    }



    @Override
    public BaseResponse<CustomerDTO> deleteCustomerByID(Long customerID) {
        BaseResponse<CustomerDTO> response = new BaseResponse<>();
        Optional<CustomerEntity> customerEntityOptional = customerRepository.findById(customerID);

        if (customerEntityOptional.isEmpty()) {
            response.setMessage(Constant.HTTP_MESSAGE.FAILED);
            response.setCode(HttpStatus.NOT_FOUND.value());
            return response;
        }

        CustomerEntity customer = customerEntityOptional.get();
        if (customer.getImageEntity() != null) {
            Optional<ImageEntity> imageEntityOptional = imageRepository.findById(customer.getImageEntity().getId());
            if (imageEntityOptional.isEmpty()) {
                response.setMessage(Constant.HTTP_MESSAGE.FAILED);
                response.setCode(HttpStatus.NOT_FOUND.value());
                return response;
            }

            // Set image entity as deleted
            ImageEntity imageEntity = imageEntityOptional.get();
            imageEntity.setDeleted(true);
            imageRepository.save(imageEntity); // Save the updated image entity
        }

        // Set customer as deleted
        customer.setDeleted(true);
        customerRepository.save(customer); // Save the updated customer entity

        // Prepare response
        CustomerDTO customerDto = modelMapper.map(customer, CustomerDTO.class);
        response.setCode(HttpStatus.OK.value());
        response.setMessage(Constant.HTTP_MESSAGE.SUCCESS);
        response.setData(customerDto);

        return response;
    }


    @Override
    public BaseResponse<?> findCustomerByID(Long customerID) {
        BaseResponse<CustomerDTO> response = new BaseResponse<>();
        Optional<CustomerEntity> customerEntity = customerRepository.findById(customerID);
        if (customerEntity.isEmpty()) {
            response.setMessage(Constant.HTTP_MESSAGE.FAILED);
            response.setCode(HttpStatus.NOT_FOUND.value());
            return response;
        }
        CustomerEntity customer = customerEntity.get();
        if (customer.getDeleted()){
            response.setMessage(Constant.HTTP_MESSAGE.FAILED);
            response.setCode(HttpStatus.NOT_FOUND.value());
            return response;
        }
        RoleEntity roleEntities = customer.getRoleEntity();
        RoleDTO roleDTO = modelMapper.map(roleEntities, RoleDTO.class);
        roleDTO.setId(roleEntities.getId());
        roleDTO.setName(roleEntities.getName());

        ImageEntity imageEntitys = customer.getImageEntity();
//        CustomerDTO customerDTO = modelMapper.map(imageEntitys, CustomerDTO.class);



        CustomerDTO customerDTO = modelMapper.map(customer, CustomerDTO.class);
        if (imageEntitys != null) {
            try {
                byte[] fileBytes = imageEntitys.getFile();
                String base64Image = Base64.getEncoder().encodeToString(fileBytes);
                customerDTO.setImage(base64Image);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        customerDTO.setRoleId(customer.getRoleEntity().getId());
        customerDTO.setRoleDtos(roleDTO);
        response.setCode(HttpStatus.OK.value());
        response.setMessage(Constant.HTTP_MESSAGE.SUCCESS);
        response.setData(customerDTO);
        return response;
    }

    @Override
    public BaseResponse<RegisterDTO> registerCustomer(RegisterDTO registerDTO) {
        BaseResponse<RegisterDTO> response = new BaseResponse<>();
        try {
            if (customerRepository.existsByUsername(registerDTO.getUsername())) {
                response.setCode(HttpStatus.BAD_REQUEST.value());
                response.setMessage(Constant.HTTP_MESSAGE.FAILED);
                return response;
            }
            CustomerEntity customer = new CustomerEntity();
            customer.setUsername(registerDTO.getUsername());
            customer.setPassword(passwordEncoder.encode(registerDTO.getPassword()));
            customer.setDeleted(false);
            RoleEntity roleEntity = roleRepository.findById(2L).orElseGet(() -> {
                RoleEntity newRole = new RoleEntity();
                newRole.setId(2L);
                newRole.setName(Constant.ROLE_USER);
                return roleRepository.save(newRole);
            });
            customer.setRoleEntity(roleEntity);
            customerRepository.save(customer);
            registerDTO.setId(customer.getId());
            RoleDTO roleDTO = modelMapper.map(roleEntity, RoleDTO.class);
            registerDTO.setRoleDtos(roleDTO);
            response.setCode(HttpStatus.OK.value());
            response.setMessage(Constant.HTTP_MESSAGE.SUCCESS);
            response.setData(registerDTO);
        } catch (Exception e) {
            response.setCode(HttpStatus.BAD_REQUEST.value());
            response.setMessage(Constant.HTTP_MESSAGE.FAILED + e.getMessage());
        }
        return response;
    }

    @Override
    public BaseResponse<List<CustomerDTO>> findCustomerByUsernameAndFullname(String condition) {
        BaseResponse<List<CustomerDTO>> response = new BaseResponse<>();
        List<CustomerEntity> customerEntities = customerRepository.findUserByUsernameAndFullname(condition);
        if (customerEntities == null &&  customerEntities.isEmpty()) {
            response.setCode(HttpStatus.NOT_FOUND.value());
            response.setMessage(Constant.HTTP_MESSAGE.FAILED);
        }
        List<CustomerDTO> customerDTOList = new ArrayList<>();
        for (CustomerEntity customerEntity : customerEntities) {
            CustomerDTO customerDTO = modelMapper.map(customerEntity, CustomerDTO.class);
            RoleEntity roleEntities = customerEntity.getRoleEntity();
            RoleDTO roleDTO = modelMapper.map(roleEntities, RoleDTO.class);
            roleDTO.setId(roleEntities.getId());
            roleDTO.setName(roleEntities.getName());
            customerDTO.setRoleId(customerEntity.getRoleEntity().getId());
            customerDTO.setRoleDtos(roleDTO);

            ImageEntity imageEntity = customerEntity.getImageEntity();
            if (imageEntity != null) {
                try {
                    byte[] fileBytes = imageEntity.getFile();
                    String base64Image = Base64.getEncoder().encodeToString(fileBytes);
                    customerDTO.setImage(base64Image);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            customerDTOList.add(customerDTO);
        }
        response.setCode(HttpStatus.OK.value());
        response.setMessage(Constant.HTTP_MESSAGE.SUCCESS);
        response.setData(customerDTOList);

        return response;
    }

    @Override
    public BaseResponse<CustomerDTO> getUser() {
        BaseResponse<CustomerDTO> response = new BaseResponse<>();
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication.getPrincipal() instanceof UserDetails) {
            UserDetails userDetails = (UserDetails) authentication.getPrincipal();
            String username = userDetails.getUsername();
            CustomerEntity customerEntity = customerRepository.findByUsername(username);
            if (customerEntity != null) {
                CustomerDTO accountDto = modelMapper.map(customerEntity, CustomerDTO.class);
                ImageEntity imageEntity = customerEntity.getImageEntity();
                if (imageEntity != null) {
                    try {
                        byte[] fileBytes = imageEntity.getFile();
                        String base64Image = Base64.getEncoder().encodeToString(fileBytes);
                        accountDto.setImage(base64Image);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                response.setData(accountDto);
                response.setCode(HttpStatus.OK.value());
                response.setMessage(Constant.HTTP_MESSAGE.SUCCESS);
                return response;
            } else {
                response.setCode(HttpStatus.BAD_REQUEST.value());
                response.setMessage(Constant.HTTP_MESSAGE.FAILED);
                return response;
            }
        }
        return response;
    }

    @Override
    public BaseResponse<?> updatePassWord(Long id, ChagePasswordDTO chagePasswordDTO) {
        BaseResponse<?> response = new BaseResponse<>();
        Optional<CustomerEntity> customerEntityOptional = customerRepository.findById(id);
        if (customerEntityOptional.isEmpty()) {
            response.setCode(HttpStatus.NOT_FOUND.value());
            response.setMessage(Constant.HTTP_MESSAGE.FAILED);
            return response;
        }

        CustomerEntity customerEntity = customerEntityOptional.get();
        String oldPassword = customerEntity.getPassword();
        String newPassword = chagePasswordDTO.getNewPassword();
        String confirmPassword = chagePasswordDTO.getConfirmPassword();

        if (!passwordEncoder.matches(chagePasswordDTO.getOldPassword(), oldPassword)) {
            response.setCode(HttpStatus.BAD_REQUEST.value());
            response.setMessage(Constant.HTTP_MESSAGE.FAILEDPW);
            return response;
        }

        if (newPassword.equals(confirmPassword)) {
            if (newPassword.equals(chagePasswordDTO.getOldPassword())) {
                response.setCode(HttpStatus.BAD_REQUEST.value());
                response.setMessage(Constant.HTTP_MESSAGE.CHECKPASSWORD);
            } else {
                customerEntity.setPassword(passwordEncoder.encode(newPassword));
                customerRepository.save(customerEntity);
                response.setCode(HttpStatus.OK.value());
                response.setMessage(Constant.HTTP_MESSAGE.SUCCESS);
            }
        } else {
            response.setCode(HttpStatus.BAD_REQUEST.value());
            response.setMessage(Constant.HTTP_MESSAGE.FAILED);
        }

        return response;
    }

}
