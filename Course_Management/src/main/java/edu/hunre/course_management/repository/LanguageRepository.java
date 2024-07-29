package edu.hunre.course_management.repository;

import edu.hunre.course_management.entity.LanguageEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface LanguageRepository extends JpaRepository<LanguageEntity,Long> {
    @Query(value = "SELECT l FROM LanguageEntity l WHERE l.deleted=false ")
    Page<LanguageEntity> findAllLanguageLevel(Pageable pageable);
}
