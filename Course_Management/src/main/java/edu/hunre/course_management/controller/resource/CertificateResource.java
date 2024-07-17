package edu.hunre.course_management.controller.resource;

import edu.hunre.course_management.model.dto.CertificateDTO;

import edu.hunre.course_management.model.response.BaseResponse;
import edu.hunre.course_management.service.ICertificateService;
import jakarta.validation.constraints.Min;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "/api/certificate")
public class CertificateResource {
    @Autowired
    private ICertificateService icertificateService;
    @GetMapping("/admin/list")
    public ResponseEntity<BaseResponse<Page<CertificateDTO>>> getAll(@RequestParam(name = "page", required = false, defaultValue = "0") int page,
                                                                     @Min(10) @RequestParam(name = "size", required = false, defaultValue = "10") int size) {
        return ResponseEntity.ok(icertificateService.getAllCertificates(page, size));
    }

    @PostMapping("/admin/certifice/{accountId}")
    public ResponseEntity<BaseResponse<?>> addCertificate(
            @PathVariable Long accountId,
            @RequestBody CertificateDTO certificateDTO) {
        BaseResponse<?> response = icertificateService.addCertificate(accountId, certificateDTO);
        return new ResponseEntity<>(response, HttpStatus.valueOf(response.getCode()));
    }
    @PutMapping("/common/update/{id}")
    public ResponseEntity<?> update(@PathVariable Long id, @RequestBody CertificateDTO certificateDTO) {
        return ResponseEntity.ok(icertificateService.updateCertificate(id, certificateDTO));
    }
    @DeleteMapping("/admin/delete/{id}")
    public ResponseEntity<?> delete(@PathVariable Long id) {
        BaseResponse<?> response = icertificateService.deleteCertificate(id);
        if (response.getCode() == HttpStatus.OK.value()) {
            return ResponseEntity.ok().body(response);
        } else {
            return ResponseEntity.status(response.getCode()).body(response.getMessage());
        }
    }
    @GetMapping("/admin/searchById/{id}")
    public ResponseEntity<BaseResponse<?>> searchById(@PathVariable Long id) {
        BaseResponse<?> response = icertificateService.findById(id);
        if (response.getCode() == HttpStatus.OK.value()) {
            return ResponseEntity.status(response.getCode()).body(response);
        }
        return ResponseEntity.notFound().build();
    }
    @GetMapping("/admin/searchByCondition/{keyword}")
    public ResponseEntity<BaseResponse<List<CertificateDTO>>> findCerByUserName(@PathVariable String keyword) {
        BaseResponse<List<CertificateDTO>> response = icertificateService.findCertificateByFUllName(keyword);
        if (response.getCode() == HttpStatus.OK.value()) {
            return ResponseEntity.ok(response);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }
    }

}
