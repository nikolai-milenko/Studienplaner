package com.training.studienplaner.assignment;

import com.training.studienplaner.course.Course;
import com.training.studienplaner.course.CourseMapper;
import com.training.studienplaner.course.CourseRepository;
import jakarta.persistence.EntityNotFoundException;
import org.mapstruct.Context;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import java.util.List;

@Mapper(componentModel = "spring", uses = {CourseMapper.class})
public interface AssignmentMapper {
    // RequestDTO -> Entity
    @Mapping(target = "course", source = "courseId", qualifiedByName = "mapCourse")
    Assignment toEntity(AssignmentRequestDto dto, @Context CourseRepository courseRepository);

    // Entity -> ResponseDTO
    AssignmentResponseDto toResponseDto(Assignment entity);
    List<AssignmentResponseDto> toResponseDto(List<Assignment> entity);

    // Entity -> ShortDTO
    AssignmentShortDto toShortDto(Assignment entity);

    @Named("mapCourse")
    default Course mapCourse(Long courseId, @Context CourseRepository courseRepository) {
        if (courseId == null) {
            return null;
        }
        return courseRepository.findById(courseId)
                .orElseThrow(() -> new EntityNotFoundException("Course not found: " + courseId));
    }
}
