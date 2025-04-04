package com.training.studienplaner.submission;

public record SubmissionRequestDto(
        Long assignmentId,
        String content
) {}
