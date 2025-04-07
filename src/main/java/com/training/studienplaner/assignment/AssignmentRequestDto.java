package com.training.studienplaner.assignment;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.time.LocalDateTime;

public record AssignmentRequestDto(
        @NotBlank(message = "Titel darf nicht leer sein")
        @Size(max = 50, message = "Titel darf maximal 50 Zeichen lang sein")
        String title,

        @NotBlank(message = "Beschreibung darf nicht leer sein")
        @Size(max = 255, message = "Beschreibung darf maximal 255 Zeichen lang sein")
        String description,

        @NotNull(message = "Typ muss angegeben sein")
        Assignment.AssignmentType type,

        @NotNull(message = "Deadline muss angegeben sein")
        LocalDateTime deadline,

        @NotNull(message = "courseId muss angegeben sein!")
        Long courseId
) {
}
