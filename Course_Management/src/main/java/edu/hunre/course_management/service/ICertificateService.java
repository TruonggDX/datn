package edu.hunre.course_management.service;

import edu.hunre.course_management.model.dto.CertificateDTO;
import edu.hunre.course_management.model.dto.CustomerDTO;
import edu.hunre.course_management.model.response.BaseResponse;
import org.springframework.data.domain.Page;

import java.util.List;

public interface ICertificateService {
    BaseResponse<Page<CertificateDTO>> getAllCertificates(int page,int size);
    BaseResponse<?> addCertificate(Long id, CertificateDTO certificateDTO);
    BaseResponse<?> updateCertificate(Long idCer,CertificateDTO certificateDTO);
    BaseResponse<?> deleteCertificate(Long idCer);
    BaseResponse<?> findById(Long idCer);
    BaseResponse<List<CertificateDTO>> findCertificateByFUllName(String condition);
}
