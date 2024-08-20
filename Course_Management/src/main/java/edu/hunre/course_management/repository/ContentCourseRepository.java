package edu.hunre.course_management.repository;

import edu.hunre.course_management.entity.ContentCourseEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface ContentCourseRepository extends JpaRepository<ContentCourseEntity,Long> {
    @Query(value = "SELECT c FROM ContentCourseEntity c WHERE c.deleted=false ")
    Page<ContentCourseEntity> findAllByDeletedFalse(Pageable pageable);
}
