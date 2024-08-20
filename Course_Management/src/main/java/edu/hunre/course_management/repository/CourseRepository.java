package edu.hunre.course_management.repository;

import edu.hunre.course_management.entity.CourseEntity;
import edu.hunre.course_management.model.request.CourseFilterRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CourseRepository extends JpaRepository<CourseEntity,Long> {
    @Query(value = "SELECT distinct p FROM CourseEntity p " +
            "LEFT JOIN p.categoryEntity c " +
            "LEFT JOIN p.languageEntity m " +
            "LEFT JOIN p.accountEntity a " +
            "LEFT JOIN p.imageEntityList i " +
            "WHERE (:#{#condition.name} is null or lower(p.name) = lower(:#{#condition.name})) " +
            "AND (:#{#condition.price} is null or p.price = :#{#condition.price}) " +
            "AND (:#{#condition.categoryId} is null or c.id = :#{#condition.categoryId}) " +
            "AND (:#{#condition.languageId} is null or m.id = :#{#condition.languageId}) " +
            "AND (:#{#condition.accountId} is null or a.id = :#{#condition.accountId}) " +
            "AND (:#{#condition.imageId} is null or i.id = :#{#condition.imageId}) " +
            "AND p.deleted = false ORDER BY p.createdDate desc"
    )
    Page<CourseEntity> findAllByFilter(@Param("condition") CourseFilterRequest filterRequest, Pageable pageable);

    @Query("SELECT c FROM CourseEntity c WHERE c.name LIKE %:name% AND c.deleted=false ")
    List<CourseEntity> findCourseByName(@Param("name") String name);


    @Query("SELECT c FROM CourseEntity c WHERE c.categoryEntity.id =:categoryId AND c.deleted=false ")
    Page<CourseEntity> findCourseByCategoryId(@Param("categoryId") Long categoryId,Pageable pageable);

    @Query("SELECT c FROM CourseEntity c WHERE " +
            "(:categoryId IS NULL OR c.categoryEntity.id = :categoryId) AND " +
            "(:levelId IS NULL OR c.levelEntity.id = :levelId) AND " +
            "(:languageId IS NULL OR c.languageEntity.id = :languageId) AND "+
            "c.deleted = false")
    Page<CourseEntity> findCourseByCategoryIdAndLevelId(@Param("categoryId") Long categoryId,Pageable pageable, @Param("levelId") Long levelId, @Param("languageId") Long languageId);

    @Query("SELECT c.levelEntity.id, COUNT(c) FROM CourseEntity c WHERE c.levelEntity.id IN :levelIds AND c.deleted = false GROUP BY c.levelEntity.id")
    List<Object[]> countCoursesByLevel(@Param("levelIds") List<Long> levelIds);

    @Query("SELECT c FROM CourseEntity c WHERE c.deleted=false AND c.wishlist=true")
    Page<CourseEntity> findAllCourseByWishList(Pageable pageable);

    @Query("SELECT c FROM CourseEntity c WHERE c.accountEntity.id=:accountId AND c.deleted=false")
    Page<CourseEntity> findCourseByAccountId(@Param("accountId") Long accountId,Pageable pageable);


    @Query("SELECT COUNT(*) FROM CourseEntity c WHERE c.deleted = false AND c.accountEntity.id=:accountId")
    Long countCourseByAccountId(@Param("accountId") Long accountId);

    @Query(value = "SELECT c FROM CourseEntity c WHERE c.deleted=false AND c.levelEntity.id=:levelId")
    Page<CourseEntity> findCourseByLevelId(@Param("levelId") Long levelId,Pageable pageable);



}
