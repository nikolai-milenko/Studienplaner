package com.training.studienplaner.assignment;
import com.training.studienplaner.course.CourseShortDto;

import java.time.LocalDateTime;

public record AssignmentResponseDto(
        Long assignmentId,
        String title,
        String description,
        Assignment.AssignmentType type,
        LocalDateTime deadline,
        CourseShortDto course
) {
}
