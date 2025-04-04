package com.training.studienplaner.user;
import com.training.studienplaner.course.CourseShortDto;

import java.util.List;

public record UserResponseDto(
        Long userId,
        String name,
        String surname,
        User.Role role,
        String email,
        List<CourseShortDto> coursesList
) {
}
