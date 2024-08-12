package edu.hunre.course_management.repository;

import edu.hunre.course_management.entity.CourseEntity;
import edu.hunre.course_management.entity.OrderEntity;
import edu.hunre.course_management.model.request.CourseFilterRequest;
import edu.hunre.course_management.model.request.OrderFilterRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderRepository extends JpaRepository<OrderEntity,Long> {
    @Query(value = "SELECT o FROM OrderEntity o " +
            "LEFT JOIN o.customerEntity c " +
            "WHERE (:#{#condition.code} is null or lower(o.code) = lower(:#{#condition.code})) " +
            "AND (:#{#condition.customerId} is null or c.id = :#{#condition.customerId}) " +
            "AND (:#{#condition.customerName} is null or c.fullname = :#{#condition.customerName}) " +
            "AND o.deleted = false ORDER BY o.createdDate desc"
    )
    Page<OrderEntity> findAllByFilter(@Param("condition") OrderFilterRequest filterRequest, Pageable pageable);
}
