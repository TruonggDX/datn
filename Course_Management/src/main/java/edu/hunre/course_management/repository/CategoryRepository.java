package edu.hunre.course_management.repository;

import edu.hunre.course_management.entity.CategoryEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CategoryRepository extends JpaRepository<CategoryEntity,Long> {
    @Query(value = "SELECT c FROM CategoryEntity c WHERE c.deleted=false ")
    Page<CategoryEntity> findAllCategory(Pageable pageable);
//    @Query("SELECT c FROM CategoryEntity c WHERE c.deleted = false AND c.parent = :parentId")
//    List<CategoryEntity> findByParentId(@Param("parentId") Long parentId);
    List<CategoryEntity> findByParentId(Long parentId);

}
