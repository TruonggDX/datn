package edu.hunre.course_management.repository;

import edu.hunre.course_management.entity.AccountEntity;
import edu.hunre.course_management.entity.CertificateEntity;
import edu.hunre.course_management.entity.CustomerEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Set;

@Repository
public interface CertificateRepository extends JpaRepository<CertificateEntity,Long> {

    @Query(value = "SELECT c FROM CertificateEntity  c WHERE c.deleted=false ")
    Page<CertificateEntity> findAllCertificate(Pageable pageable);

    @Query(value = "SELECT c FROM CertificateEntity c JOIN c.accountEntity a WHERE ( a.fullname LIKE %:condition% OR c.certificateName LIKE %:condition%) AND c.deleted=false")
    List<CertificateEntity> findAllCertificateByFullName(@Param("condition") String condition);
}
