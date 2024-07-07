package edu.hunre.course_management.entity;

import jakarta.persistence.*;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

@Entity
@Setter
@Getter
@Table(name = "customer")
public class CustomerEntity extends AbstractEntity {
    private String fullname;
    private String username;
    private String phone;
    private String address;
    private String password;
    private String certificate;

    @ManyToOne
    @JoinColumn(name = "role_id")
    @EqualsAndHashCode.Exclude
    private RoleEntity roleEntity;

    @OneToOne(mappedBy = "customer", cascade = CascadeType.ALL)
    private ImageEntity imageEntity;

}