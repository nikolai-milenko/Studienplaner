package com.training.studienplaner.course;
import com.training.studienplaner.assignment.AssignmentMapper;
import com.training.studienplaner.user.UserMapper;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring", uses = {UserMapper.class, AssignmentMapper.class})
public interface CourseMapper {
    // DTO → Entity
    Course toEntity(CourseRequestDto dto);

    // Entity → Response DTO
    @Mapping(source = "tutor", target = "tutor")
    @Mapping(source = "assignments", target = "assignments")
    @Mapping(source = "students", target = "students")
    CourseResponseDto toResponseDto(Course course);
    List<CourseResponseDto> toResponseDto(List<Course> courses);

    // Entity → Short DTO
    CourseShortDto toShortDto(Course course);

}
