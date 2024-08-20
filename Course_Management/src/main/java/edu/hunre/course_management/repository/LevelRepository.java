package edu.hunre.course_management.repository;

import edu.hunre.course_management.entity.CourseEntity;
import edu.hunre.course_management.entity.LevelEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LevelRepository extends JpaRepository<LevelEntity,Long> {
    @Query(value = "SELECT l FROM LevelEntity l WHERE l.deleted=false ")
    List<LevelEntity> findAllLevel();
}
