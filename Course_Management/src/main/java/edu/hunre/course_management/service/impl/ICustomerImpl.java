package edu.hunre.course_management.service.impl;

import edu.hunre.course_management.entity.CustomerEntity;
import edu.hunre.course_management.entity.ImageEntity;
import edu.hunre.course_management.entity.RoleEntity;
import edu.hunre.course_management.model.dto.CustomerDTO;
import edu.hunre.course_management.model.dto.ImageDTO;
import edu.hunre.course_management.model.dto.RoleDTO;
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
//import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ICustomerImpl implements ICustomerService {
    @Autowired
    private CustomerRepository customerRepository;
    @Autowired
    private RoleRepository roleRepository;
//    @Autowired
//    private PasswordEncoder passwordEncoder;
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
        customerEntity.setCertificate(customerDTO.getCertificate());
        customerEntity.setAddress(customerDTO.getAddress());
        customerEntity.setDeleted(false);

        Optional<RoleEntity> roleEntity = roleRepository.findById(customerDTO.getRoleId());

        if (roleEntity.isEmpty()) {
            response.setCode(HttpStatus.BAD_REQUEST.value());
            response.setMessage(Constant.HTTP_MESSAGE.FAILED);
            return response;
        }

        customerEntity.setRoleEntity(roleEntity.get());
        customerRepository.save(customerEntity);
        CustomerDTO customerDTOs = modelMapper.map(customerEntity, CustomerDTO.class);
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

//    @Override
//    public BaseResponse<CustomerDTO> updateCustomer(Long id, CustomerDTO customerDTO) {
//        BaseResponse<CustomerDTO> response = new BaseResponse<>();
//        Optional<CustomerEntity> customerEntity = customerRepository.findById(id);
//        if(customerEntity.isEmpty()){
//            response.setMessage(Constant.HTTP_MESSAGE.FAILED);
//            response.setCode(HttpStatus.NOT_FOUND.value());
//            return response;
//        }
//        CustomerEntity customer = customerEntity.get();
//        customer.setFullname(customerDTO.getFullname());
//        customer.setPhone(customerDTO.getPhone());
//        customer.setAddress(customerDTO.getAddress());
//        customer.setCertificate(customer.getCertificate());
//
//        Optional<RoleEntity> roleEntity = roleRepository.findById(customerDTO.getRoleId());
//        if (roleEntity.isPresent()) {
//            customer.setRoleEntity(roleEntity.get());
//        } else {
//            response.setCode(HttpStatus.BAD_REQUEST.value());
//            response.setMessage(Constant.HTTP_MESSAGE.FAILED);
//            return response;
//        }
//
//        customerRepository.save(customer);
//        CustomerDTO customerDtos = modelMapper.map(customer, CustomerDTO.class);
//        RoleEntity roleEntities = customer.getRoleEntity();
//        RoleDTO roleDTO = modelMapper.map(roleEntities, RoleDTO.class);
//        roleDTO.setId(roleEntities.getId());
//        roleDTO.setName(roleEntities.getName());
//        customerDtos.setRoleDtos(roleDTO);
//        response.setMessage(Constant.HTTP_MESSAGE.SUCCESS);
//        response.setCode(HttpStatus.OK.value());
//        response.setData(customerDtos);
//        return response;
//    }

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
        customer.setFullname(customerDTO.getFullname());
        customer.setPhone(customerDTO.getPhone());
        customer.setAddress(customerDTO.getAddress());
        customer.setCertificate(customerDTO.getCertificate());

        Optional<RoleEntity> roleEntityOptional = roleRepository.findById(customerDTO.getRoleId());
        if (roleEntityOptional.isEmpty()) {
            response.setCode(HttpStatus.BAD_REQUEST.value());
            response.setMessage(Constant.HTTP_MESSAGE.FAILED);
            return response;
        }
        customer.setRoleEntity(roleEntityOptional.get());

        Optional<ImageEntity> imageEntityOptionaly = imageRepository.findById(customerDTO.getImage_id());
        if (imageEntityOptionaly.isEmpty()) {
            response.setCode(HttpStatus.BAD_REQUEST.value());
            response.setMessage(Constant.HTTP_MESSAGE.FAILED);
            return response;
        }
        if (imageFile != null && !imageFile.isEmpty()) {
            try {
                ImageDTO uploadedImage = imageService.uploadFile(imageFile);
                ImageEntity imageEntity = modelMapper.map(uploadedImage, ImageEntity.class);
                imageEntity.setCustomer(customer);

                imageRepository.save(imageEntity);
                customer.setImageEntity(imageEntity);
            } catch (IOException e) {
                response.setCode(HttpStatus.INTERNAL_SERVER_ERROR.value());
                response.setMessage(Constant.HTTP_MESSAGE.FAILED);
                return response;
            }
        }
        customerRepository.save(customer);
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
    }


    @Override
    public BaseResponse<CustomerDTO> deleteCustomerByID(Long customerID) {
        BaseResponse<CustomerDTO> response = new BaseResponse<>();
        Optional<CustomerEntity> customerEntity = customerRepository.findById(customerID);
        if (customerEntity.isEmpty()) {
            response.setMessage(Constant.HTTP_MESSAGE.FAILED);
            response.setCode(HttpStatus.NOT_FOUND.value());
            return response;
        }
        CustomerEntity customer = customerEntity.get();
        customer.setDeleted(true);
        customerRepository.save(customer);
        CustomerDTO customerDtos = modelMapper.map(customer, CustomerDTO.class);
        response.setCode(HttpStatus.OK.value());
        response.setMessage(Constant.HTTP_MESSAGE.SUCCESS);
        response.setData(customerDtos);
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


        CustomerDTO customerDTO = modelMapper.map(customer, CustomerDTO.class);
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
//            customer.setPassword(passwordEncoder.encode(registerDTO.getPassword()));
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
            customerDTOList.add(customerDTO);
        }
        response.setCode(HttpStatus.OK.value());
        response.setMessage(Constant.HTTP_MESSAGE.SUCCESS);
        response.setData(customerDTOList);

        return response;
    }
}
