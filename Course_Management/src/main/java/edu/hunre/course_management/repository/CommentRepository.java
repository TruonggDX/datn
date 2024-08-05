package edu.hunre.course_management.repository;

import edu.hunre.course_management.entity.CommentEntity;
import edu.hunre.course_management.entity.RatingEntity;
import edu.hunre.course_management.model.dto.CommentDTO;
import edu.hunre.course_management.model.dto.RatingDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface CommentRepository extends JpaRepository<CommentEntity,Long> {
    @Query(value = "SELECT distinct r FROM CommentEntity r " +
            "LEFT JOIN r.customerEntity c " +
            "LEFT JOIN r.courseEntity m " +
            "LEFT JOIN r.ratingEntity a " +
            "WHERE (:#{#condition.content} is null or lower(r.content) = lower(:#{#condition.content})) " +
            "AND (:#{#condition.customerId} is null or c.id = :#{#condition.customerId}) " +
            "AND (:#{#condition.courseId} is null or m.id = :#{#condition.courseId}) " +
            "AND (:#{#condition.rateId} is null or a.id IN :#{#condition.rateId}) " +
            "AND r.deleted = false ORDER BY r.createdDate desc"
    )
    Page<CommentEntity> findAllByFilter(@Param("condition") CommentDTO commentDTO, Pageable pageable);

}
