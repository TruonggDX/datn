package edu.hunre.course_management.service.impl;

import edu.hunre.course_management.entity.*;
import edu.hunre.course_management.model.dto.CertificateDTO;
import edu.hunre.course_management.model.dto.RoleDTO;
import edu.hunre.course_management.model.response.BaseResponse;
import edu.hunre.course_management.repository.AccountRepository;
import edu.hunre.course_management.repository.CertificateRepository;
import edu.hunre.course_management.service.ICertificateService;
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
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;


@Service
public class ICertificateImpl implements ICertificateService {

    @Autowired
    private ModelMapper modelMapper;
    @Autowired
    private CertificateRepository certificateRepository;
    @Autowired
    private AccountRepository accountRepository;


    @Override
    public BaseResponse<Page<CertificateDTO>> getAllCertificates(int page, int size) {
        Pageable pageable = PageRequest.of(page,size);
        Page<CertificateEntity> pages = certificateRepository.findAllCertificate(pageable);
        List<CertificateDTO> certificateDtos = pages.getContent().stream().map(certificateEntity -> {
            CertificateDTO certificateDTO = modelMapper.map(certificateEntity,CertificateDTO.class);
            Long accountId = certificateEntity.getAccountEntity().getId();
            certificateDTO.setAccount_id(accountId);
            String accountName = certificateEntity.getAccountEntity().getFullname();
            certificateDTO.setAccountName(accountName);
            return certificateDTO;
        }).collect(Collectors.toList());
        BaseResponse<Page<CertificateDTO>> response = new BaseResponse<>();
        response.setCode(HttpStatus.OK.value());
        response.setMessage(Constant.HTTP_MESSAGE.SUCCESS);
        response.setData(new PageImpl<>(certificateDtos, pageable, pages.getTotalElements()));
        return response;
    }

    @Override
    public BaseResponse<?> addCertificate(Long id, CertificateDTO certificateDTO) {
        BaseResponse<CertificateDTO> response = new BaseResponse<>();
        Optional<AccountEntity> optionalUser = accountRepository.findById(id);
        if(optionalUser.isEmpty()){
            response.setCode(HttpStatus.BAD_REQUEST.value());
            response.setMessage(Constant.HTTP_MESSAGE.FAILED);
            return response;
        }
        AccountEntity accountEntity = optionalUser.get();
        CertificateEntity certificateEntity = new CertificateEntity();
        certificateEntity.setCertificateName(certificateDTO.getCertificateName());
        certificateEntity.setDescription(certificateDTO.getDescription());
        certificateEntity.setIssuingOrganization(certificateDTO.getIssuingOrganization());
        certificateEntity.setCertificateType(certificateDTO.getCertificateType());
        certificateEntity.setCertificateNumber(certificateDTO.getCertificateNumber());
        certificateEntity.setIssueDate(certificateDTO.getIssueDate());
        certificateEntity.setCertificateStatus(certificateDTO.getCertificateStatus());
        certificateEntity.setDeleted(false);
        certificateEntity.setCreatedDate(LocalDateTime.now());
        certificateEntity.setAccountEntity(accountEntity);
        accountEntity.getCertificateEntities().add(certificateEntity);
        certificateDTO.setAccount_id(certificateEntity.getAccountEntity().getId());
        certificateEntity = certificateRepository.save(certificateEntity);
        Long certificateId = certificateEntity.getId();
        certificateDTO.setId(certificateId);
        accountRepository.save(accountEntity);
        response.setCode(HttpStatus.OK.value());
        response.setMessage(Constant.HTTP_MESSAGE.SUCCESS);
        response.setData(certificateDTO);
        return response;
    }



    @Override
    public BaseResponse<?> updateCertificate(Long idCer, CertificateDTO certificateDTO) {
        BaseResponse<CertificateDTO> response = new BaseResponse<>();
        Optional<CertificateEntity> certificate = certificateRepository.findById(idCer);
        if (certificate.isEmpty()){
            response.setCode(HttpStatus.NOT_FOUND.value());
            response.setMessage(Constant.HTTP_MESSAGE.FAILED);
            return response;
        }
        CertificateEntity certificateEntity = certificate.get();
        certificateEntity.setCertificateName(certificateDTO.getCertificateName());
        certificateEntity.setCertificateType(certificateDTO.getCertificateType());
        certificateEntity.setCertificateStatus(certificateDTO.getCertificateStatus());
        certificateEntity.setDescription(certificateDTO.getDescription());
        certificateEntity.setIssuingOrganization(certificateDTO.getIssuingOrganization());
        certificateEntity.setIssueDate(certificateDTO.getIssueDate());
        certificateEntity.setCertificateNumber(certificateDTO.getCertificateNumber());
        certificateEntity.setDeleted(false);
        certificateEntity.setModifiedDate(LocalDateTime.now());

        certificateRepository.save(certificateEntity);
        CertificateDTO certificateDto = modelMapper.map(certificateEntity,CertificateDTO.class);
        certificateDto.setAccount_id(certificateEntity.getAccountEntity().getId());
        response.setData(certificateDto);
        response.setMessage(Constant.HTTP_MESSAGE.SUCCESS);
        response.setCode(HttpStatus.OK.value());

        return response;
    }

    @Override
    public BaseResponse<?> deleteCertificate(Long idCer) {
        BaseResponse<CertificateDTO> response = new BaseResponse<>();
        Optional<CertificateEntity> certificate = certificateRepository.findById(idCer);
        if (certificate.isEmpty()){
            response.setCode(HttpStatus.NOT_FOUND.value());
            response.setMessage(Constant.HTTP_MESSAGE.FAILED);
            return response;
        }
        CertificateEntity certificateEntity = certificate.get();
        certificateEntity.setDeleted(true);
        certificateEntity.setModifiedDate(LocalDateTime.now());
        certificateRepository.save(certificateEntity);
        CertificateDTO certificateDto = modelMapper.map(certificateEntity,CertificateDTO.class);
        certificateDto.setAccount_id(certificateEntity.getAccountEntity().getId());
        certificateDto.setAccountName(certificateEntity.getAccountEntity().getFullname());
        response.setData(certificateDto);
        response.setMessage(Constant.HTTP_MESSAGE.SUCCESS);
        response.setCode(HttpStatus.OK.value());
        return response;
    }

    @Override
    public BaseResponse<?> findById(Long idCer) {
        BaseResponse<CertificateDTO> response = new BaseResponse<>();
        Optional<CertificateEntity> certificate = certificateRepository.findById(idCer);
        if (certificate.isEmpty()){
            response.setCode(HttpStatus.NOT_FOUND.value());
            response.setMessage(Constant.HTTP_MESSAGE.FAILED);
            return response;
        }
        CertificateEntity certificateEntity = certificate.get();
        if (certificateEntity.getDeleted()){
            response.setCode(HttpStatus.NOT_FOUND.value());
            response.setMessage(Constant.HTTP_MESSAGE.FAILED);
            return response;
        }
        CertificateDTO certificateDto = modelMapper.map(certificateEntity,CertificateDTO.class);
        certificateDto.setAccount_id(certificateEntity.getAccountEntity().getId());
        certificateDto.setAccountName(certificateEntity.getAccountEntity().getFullname());
        response.setData(certificateDto);
        response.setMessage(Constant.HTTP_MESSAGE.SUCCESS);
        response.setCode(HttpStatus.OK.value());
        return response;
    }

    @Override
    public BaseResponse<List<CertificateDTO>> findCertificateByFUllName(String condition) {
        BaseResponse<List<CertificateDTO>> response = new BaseResponse<>();
        List<CertificateEntity> certificateEntities = certificateRepository.findAllCertificateByFullName(condition);
        if (certificateEntities == null &&  certificateEntities.isEmpty()) {
            response.setCode(HttpStatus.NOT_FOUND.value());
            response.setMessage(Constant.HTTP_MESSAGE.FAILED);
        }
        List<CertificateDTO> customerDTOList = new ArrayList<>();
        for (CertificateEntity certificateEntity : certificateEntities) {
            CertificateDTO customerDTO = modelMapper.map(certificateEntity, CertificateDTO.class);
            customerDTO.setAccount_id(certificateEntity.getAccountEntity().getId());
            customerDTO.setAccountName(certificateEntity.getAccountEntity().getFullname());
            customerDTOList.add(customerDTO);
        }
        response.setData(customerDTOList);
        response.setMessage(Constant.HTTP_MESSAGE.SUCCESS);
        response.setCode(HttpStatus.OK.value());
        return response;
    }
}
