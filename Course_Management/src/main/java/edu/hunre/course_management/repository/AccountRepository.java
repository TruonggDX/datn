package edu.hunre.course_management.repository;

import edu.hunre.course_management.entity.AccountEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AccountRepository extends JpaRepository<AccountEntity,Long> {
    AccountEntity findByUsername(String username);

    @Query(value = "SELECT u FROM AccountEntity u WHERE u.deleted=false")
    Page<AccountEntity> listUser(Pageable pageable);

    @Query(value = "SELECT u FROM AccountEntity u WHERE (u.username LIKE %:condition% OR u.fullname LIKE %:condition%) AND u.deleted = false ")
    List<AccountEntity> findUserByUsernameAndFullname(@Param("condition") String condition);
}
