package com.training.studienplaner.user;

import com.training.studienplaner.course.CourseMapper;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring", uses = CourseMapper.class)
public interface UserMapper {

    // DTO → Entity
    User toEntity(UserRequestDto dto);

    // Entity → Response DTO
    UserResponseDto toResponseDto(User user);

    // Entity → Short DTO
    UserShortDto toShortDto(User user);

    List<UserResponseDto> toResponseDto(List<User> users);
}
