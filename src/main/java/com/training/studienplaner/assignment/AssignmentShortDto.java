package com.training.studienplaner.assignment;

import com.training.studienplaner.course.CourseShortDto;

import java.time.LocalDateTime;

public record AssignmentShortDto(
        String title,
        Assignment.AssignmentType type,
        LocalDateTime deadline,
        CourseShortDto course
) {
}
