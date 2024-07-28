package edu.hunre.course_management.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Date;
@Entity
@Data
@Table(name = "certificate")
public class CertificateEntity extends AbstractEntity {

    @Column(name = "name")
    private String certificateName;

    @Column(name = "description")
    private String description;

    @Column(name = "organization")
    private String issuingOrganization;

    @Column(name = "type")
    private String certificateType;

    @Column(name = "number")
    private String certificateNumber;

    @Column(name = "issue_date")
    private String issueDate;

    @Column(name = "status")
    private String certificateStatus;

    @ManyToOne
    @JoinColumn(name = "account_id")
    @EqualsAndHashCode.Exclude
    private AccountEntity accountEntity;
}
