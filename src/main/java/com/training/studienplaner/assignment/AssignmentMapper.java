package com.training.studienplaner.assignment;

import com.training.studienplaner.course.CourseMapper;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring", uses = {CourseMapper.class})
public interface AssignmentMapper {
    // RequestDTO -> Entity
    Assignment toEntity(AssignmentRequestDto dto);

    // Entity -> ResponseDTO
    AssignmentResponseDto toResponseDto(Assignment entity);
    List<AssignmentResponseDto> toResponseDto(List<Assignment> entity);

    // Entity -> ShortDTO
    AssignmentShortDto toShortDto(Assignment entity);
}
