package edu.hunre.course_management.repository;

import edu.hunre.course_management.entity.CourseEntity;
import edu.hunre.course_management.model.request.CourseFilterRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface CourseRepository extends JpaRepository<CourseEntity,Long> {
    @Query(value = "SELECT distinct p FROM CourseEntity p " +
            "LEFT JOIN p.categoryEntity c " +
            "LEFT JOIN p.languageEntity m " +
            "LEFT JOIN p.imageEntityList i " +
            "WHERE (:#{#condition.name} is null or lower(p.name) = lower(:#{#condition.name})) " +
            "AND (:#{#condition.price} is null or p.price = :#{#condition.price}) " +
            "AND (:#{#condition.categoryId} is null or c.id = :#{#condition.categoryId}) " +
            "AND (:#{#condition.languageId} is null or m.id = :#{#condition.languageId}) " +
            "AND (:#{#condition.imageId} is null or i.id = :#{#condition.imageId}) " +
            "AND p.deleted = false ORDER BY p.createdDate desc"
    )
    Page<CourseEntity> findAllByFilter(@Param("condition") CourseFilterRequest filterRequest, Pageable pageable);
}
