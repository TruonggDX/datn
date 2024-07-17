package edu.hunre.course_management.model.dto;

import lombok.Data;

import java.util.Date;

@Data
public class CertificateDTO {
    private Long id;
    private Long account_id;
    private String certificateName;
    private String description;
    private String issuingOrganization;
    private String certificateType;
    private String certificateNumber;
    private Date issueDate;
    private String certificateStatus;
    private String accountName;

}
