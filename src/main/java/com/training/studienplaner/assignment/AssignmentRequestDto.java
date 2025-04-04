package com.training.studienplaner.assignment;

import java.time.LocalDateTime;

public record AssignmentRequestDto(
        String title,
        String description,
        Assignment.AssignmentType type,
        LocalDateTime deadline,
        Long courseId
) {
}
