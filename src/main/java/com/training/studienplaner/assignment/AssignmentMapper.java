package com.training.studienplaner.assignment;

import com.training.studienplaner.course.CourseMapper;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = {CourseMapper.class})
public interface AssignmentMapper {
    // RequestDTO -> Entity
    Assignment toEntity(AssignmentRequestDto dto);

    // Entity -> ResponseDTO
    @Mapping(source = "course", target = "course")
    AssignmentResponseDto toDto(Assignment entity);

    // Entity -> ShortDTO
    @Mapping(source = "course", target = "course")
    AssignmentShortDto toShortDto(Assignment entity);
}
