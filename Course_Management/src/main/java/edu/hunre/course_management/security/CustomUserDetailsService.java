//package edu.hunre.course_management.security;
//
//import edu.hunre.course_management.entity.RoleEntity;
//import edu.hunre.course_management.entity.UserEntity;
//import edu.hunre.course_management.repository.RoleRepository;
//import edu.hunre.course_management.repository.UserRepository;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.security.core.GrantedAuthority;
//import org.springframework.security.core.authority.SimpleGrantedAuthority;
//import org.springframework.security.core.userdetails.UserDetails;
//import org.springframework.security.core.userdetails.UserDetailsService;
//import org.springframework.security.core.userdetails.UsernameNotFoundException;
//import org.springframework.stereotype.Service;
//
//import java.util.ArrayList;
//import java.util.Collection;
//import java.util.List;
//
//@Service
//public class CustomUserDetailsService implements UserDetailsService {
//    @Autowired
//    public UserRepository userRepository;
//    @Autowired
//    public RoleRepository repository;
//    @Override
//    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
//        UserEntity userEntity = userRepository.findByUsername(username);
//        List<RoleEntity> roleEntity = repository.getRoleByUsername(username);
//        if (userEntity != null){
//            UserDetails userDetails = new org.springframework.security.core.userdetails.User(
//                    userEntity.getUsername(),
//                    userEntity.getPassword(),
//                    convertStrToAuthor(roleEntity)
//            );
//            return  userDetails;
//        }else {
//            throw  new UsernameNotFoundException("Invalid user with username!");
//        }
//    }
//
//    private Collection<? extends GrantedAuthority> convertStrToAuthor(Collection<RoleEntity> roles){
//        List<RoleEntity> roleEntities = roles.stream().toList();
//        List<SimpleGrantedAuthority> roleConfigSecurity = new ArrayList<>();
//        for (int i = 0; i < roles.size(); i++) {
//            RoleEntity roleEntity = roleEntities.get(i);
//            SimpleGrantedAuthority simpleGrantedAuthority = new SimpleGrantedAuthority("ROLE_" + roleEntity.getName());
//            roleConfigSecurity.add(simpleGrantedAuthority);
//        }
//        return roleConfigSecurity;
//    }
//}
