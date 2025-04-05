package com.training.studienplaner.user;

import com.training.studienplaner.course.CourseMapper;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = CourseMapper.class)
public interface UserMapper {

    // DTO → Entity
    User toEntity(UserRequestDto dto);

    // Entity → Response DTO
    @Mapping(source = "coursesList", target = "coursesList")
    UserResponseDto toResponseDto(User user);

    // Entity → Short DTO
    UserShortDto toShortDto(User user);
}
