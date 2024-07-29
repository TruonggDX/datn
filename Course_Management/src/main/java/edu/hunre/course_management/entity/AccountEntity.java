package edu.hunre.course_management.entity;

import jakarta.persistence.*;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Getter
@Setter
@Entity
@Table(name = "account")
public class AccountEntity extends AbstractEntity{
    private String fullname;
    private String username;
    private String phone;
    private String address;
    private String password;
    private String birthday;
    private String email;
    private String description;
    private String title;


    @ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE}, fetch = FetchType.EAGER)
    @JoinTable(
            name = "account_role",
            joinColumns = @JoinColumn(name = "account_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id")
    )
    private Set<RoleEntity> roles = new HashSet<>();


    @OneToMany(mappedBy = "accountEntity", cascade = CascadeType.ALL,fetch = FetchType.LAZY)
    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    private List<CertificateEntity> certificateEntities;


    @OneToOne(mappedBy = "accountEntity", cascade = CascadeType.ALL)
    private ImageEntity imageEntity;

    @OneToMany(mappedBy = "accountEntity")
    private List<CourseEntity> courses;

}
