package edu.hunre.course_management.mapper.decorator;

import edu.hunre.course_management.entity.CourseEntity;
import edu.hunre.course_management.mapper.CourseMapper;
import edu.hunre.course_management.mapper.ImageSourseMapper;
import edu.hunre.course_management.model.dto.CourseDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import java.util.Base64;
import java.util.List;
import java.util.stream.Collectors;

@Component
public abstract class CourseMapperDecorator implements CourseMapper {
    @Autowired
    @Qualifier("delegate")
    private CourseMapper delegate;

    @Autowired
    private ImageSourseMapper imageSourseMapper;

    @Override
    public CourseDTO toDTO(CourseEntity courseEntity) {
        CourseDTO courseDTO = delegate.toDTO(courseEntity);
        if (courseEntity.getImageEntityList() != null) {
            List<String> imageBase64Strings = courseEntity.getImageEntityList()
                    .stream()
                    .map(image -> Base64.getEncoder().encodeToString(image.getFile()))
                    .collect(Collectors.toList());

            List<Long> imageIds = courseEntity.getImageEntityList()
                    .stream()
                    .map(image -> image.getId())
                    .collect(Collectors.toList());

            courseDTO.setImageFile(imageBase64Strings);
            courseDTO.setImageId(imageIds);
        }

        return courseDTO;
    }

    @Override
    public CourseEntity toEntity(CourseDTO courseDTO) {
        return delegate.toEntity(courseDTO);
    }
}
