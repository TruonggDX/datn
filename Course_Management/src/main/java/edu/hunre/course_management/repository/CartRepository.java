package edu.hunre.course_management.repository;

import edu.hunre.course_management.entity.CartEntity;
import edu.hunre.course_management.entity.CourseEntity;
import edu.hunre.course_management.entity.CustomerEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CartRepository extends JpaRepository<CartEntity,Long> {
    @Query("SELECT c FROM CartEntity c WHERE c.customerEntity.id = :customerId AND c.deleted=false ")
    Page<CartEntity> getCourseOfCustomerInCart(@Param("customerId") Long customerId, Pageable pageable);

    @Query("SELECT COUNT(*) FROM CartEntity c WHERE c.customerEntity.id =:customerId AND c.deleted=false ")
    Long countCourseByCustomerId(@Param("customerId") Long customerId);

    @Query("SELECT c FROM CartEntity c WHERE c.customerEntity = :customer AND c.courseEntity = :course")
    Optional<CartEntity> findByCustomerEntityAndCourseEntity(@Param("customer") CustomerEntity customerEntity, @Param("course") CourseEntity courseEntity);
}
