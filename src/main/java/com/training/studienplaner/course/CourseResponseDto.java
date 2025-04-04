package com.training.studienplaner.course;
import com.training.studienplaner.assignment.AssignmentShortDto;
import com.training.studienplaner.user.UserShortDto;

import java.util.List;

public record CourseResponseDto(
        Long courseId,
        String title,
        String description,
        UserShortDto tutor,
        short ects,
        List<AssignmentShortDto> assignments,
        List<UserShortDto> students
) {
}
