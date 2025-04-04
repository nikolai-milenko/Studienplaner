package com.training.studienplaner.course;

public record CourseRequestDto(
        String title,
        String description,
        Long tutorId,
        short ects
) {
}
