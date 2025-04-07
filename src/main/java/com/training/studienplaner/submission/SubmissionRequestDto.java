package com.training.studienplaner.submission;

import com.training.studienplaner.assignment.ExistingAssignmentId;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record SubmissionRequestDto(
        @NotNull(message = "Assignment-ID darf nicht null sein")
        @ExistingAssignmentId
        Long assignmentId,

        @NotBlank(message = "Inhalt darf nicht leer sein")
        String content
) {}
