package edu.hunre.course_management.repository;

import edu.hunre.course_management.entity.ImageCourseEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ImageCourseRepository extends JpaRepository<ImageCourseEntity,Long> {

}
