package edu.hunre.course_management.repository;

import edu.hunre.course_management.entity.OrderDetailEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderDetailRepository extends JpaRepository<OrderDetailEntity,Long> {
    @Query(value = "SELECT od FROM OrderDetailEntity od " +
            "LEFT JOIN od.courseEntity p " +
            "LEFT JOIN od.courseEntity s " +
            "WHERE " +
            "(:#{#orderId} is null or od.orderEntity.id = :#{#orderId}) " +
            "AND od.deleted = false")
    List<OrderDetailEntity> findByCode(Long orderId);

}
