package edu.hunre.course_management.repository;

import edu.hunre.course_management.entity.AccountEntity;
import edu.hunre.course_management.entity.CustomerEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CustomerRepository extends JpaRepository<CustomerEntity,Long> {
    CustomerEntity findByUsername(String username);
    boolean existsByUsername(String username);

    @Query(value = "SELECT c FROM CustomerEntity c WHERE c.deleted=false ")
    Page<CustomerEntity> findAll(Pageable pageable);

    @Query(value = "SELECT c FROM CustomerEntity c WHERE (c.username LIKE %:condition% OR c.fullname LIKE %:condition%) AND c.deleted = false ")
    List<CustomerEntity> findUserByUsernameAndFullname(@Param("condition") String condition);
}
