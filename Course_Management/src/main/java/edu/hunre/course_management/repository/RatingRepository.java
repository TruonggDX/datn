package edu.hunre.course_management.repository;

import edu.hunre.course_management.entity.RatingEntity;
import edu.hunre.course_management.model.dto.RatingDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface RatingRepository extends JpaRepository<RatingEntity,Long> {
    @Query(value = "SELECT r FROM RatingEntity r " +
            "LEFT JOIN r.customerEntity c " +
            "LEFT JOIN r.courseEntity m " +
            "WHERE (:#{#condition.star} is null or r.star = :#{#condition.star}) " +
            "AND (:#{#condition.customerId} is null or c.id = :#{#condition.customerId}) " +
            "AND (:#{#condition.courseId} is null or m.id = :#{#condition.courseId}) " +
            "AND r.deleted = false ORDER BY r.createdDate desc"
    )
    Page<RatingEntity> findAllByFilter(@Param("condition") RatingDTO ratingDTO, Pageable pageable);
}
