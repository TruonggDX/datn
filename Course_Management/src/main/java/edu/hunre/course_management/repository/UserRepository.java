package edu.hunre.course_management.repository;

import edu.hunre.course_management.entity.UserEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<UserEntity,Long> {
    UserEntity findByUsername(String username);

    @Query(value = "SELECT u FROM UserEntity u WHERE u.deleted=false")
    Page<UserEntity> listUser(Pageable pageable);
}
