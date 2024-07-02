package edu.hunre.course_management.entity;

import jakarta.persistence.*;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Getter
@Setter
@Table(name = "role")
public class RoleEntity extends AbstractEntity{
    private String name;

    @ManyToMany(mappedBy = "roles")
    private Set<AccountEntity> users = new HashSet<>();

    @OneToMany(mappedBy = "roleEntity", cascade = CascadeType.ALL,fetch = FetchType.LAZY)
    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    private List<CustomerEntity> customerEntities;
}
