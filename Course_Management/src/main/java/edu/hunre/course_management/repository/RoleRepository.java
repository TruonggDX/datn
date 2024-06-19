package edu.hunre.course_management.repository;

import edu.hunre.course_management.entity.RoleEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RoleRepository extends JpaRepository<RoleEntity,Long> {
    @Query(value = "select r from RoleEntity r join r.users u where u.username=:username")
    List<RoleEntity> getRoleByUsername(@Param("username") String username);

    @Query(value = "SELECT r FROM RoleEntity r WHERE r.deleted=false ")
    Page<RoleEntity> getAllRole(Pageable pageable);
}
