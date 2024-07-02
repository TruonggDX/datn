package edu.hunre.course_management.security;

import edu.hunre.course_management.entity.CustomerEntity;
import edu.hunre.course_management.entity.RoleEntity;
import edu.hunre.course_management.entity.AccountEntity;
import edu.hunre.course_management.repository.CustomerRepository;
import edu.hunre.course_management.repository.RoleRepository;
import edu.hunre.course_management.repository.AccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

@Service
public class CustomUserDetailsService implements UserDetailsService {
    @Autowired
    public AccountRepository accountRepository;
    @Autowired
    public RoleRepository repository;
    @Autowired
    public CustomerRepository customerRepository;
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        AccountEntity userEntity = accountRepository.findByUsername(username);
        CustomerEntity customerEntity = customerRepository.findByUsername(username);

        List<RoleEntity> roleEntities = repository.getRoleByUsername(username);

        if (userEntity != null) {
            UserDetails userDetails = new org.springframework.security.core.userdetails.User(
                    userEntity.getUsername(),
                    userEntity.getPassword(),
                    convertStrToAuthor(roleEntities)
            );
            return userDetails;
        } else if (customerEntity != null) {
            RoleEntity roleEntity = repository.findByRoleCustomer(username);
            UserDetails userDetails = new org.springframework.security.core.userdetails.User(
                    customerEntity.getUsername(),
                    customerEntity.getPassword(),
                    convertStrToAuthors(Collections.singletonList(roleEntity))
            );
            return userDetails;
        } else {
            throw new UsernameNotFoundException("Invalid user with username!");
        }
    }


    private Collection<? extends GrantedAuthority> convertStrToAuthor(Collection<RoleEntity> roles){
        List<RoleEntity> roleEntities = roles.stream().toList();
        List<SimpleGrantedAuthority> roleConfigSecurity = new ArrayList<>();
        for (int i = 0; i < roles.size(); i++) {
            RoleEntity roleEntity = roleEntities.get(i);
            SimpleGrantedAuthority simpleGrantedAuthority = new SimpleGrantedAuthority("ROLE_" + roleEntity.getName());
            roleConfigSecurity.add(simpleGrantedAuthority);
        }
        return roleConfigSecurity;
    }


    private Collection<? extends GrantedAuthority> convertStrToAuthors(Collection<RoleEntity> roles) {
        Collection<SimpleGrantedAuthority> authorities = new ArrayList<>();
        for (RoleEntity roleEntity : roles) {
            SimpleGrantedAuthority authority = new SimpleGrantedAuthority("ROLE_" + roleEntity.getName());
            authorities.add(authority);
        }

        return authorities;
    }

}
