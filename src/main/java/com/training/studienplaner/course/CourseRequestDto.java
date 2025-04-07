package com.training.studienplaner.course;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record CourseRequestDto(
        @NotBlank(message = "Titel darf nicht leer sein")
        @Size(max = 50, message = "Titel darf maximal 50 Zeichen lang sein")
        String title,

        @NotBlank(message = "Beschreibung darf nicht leer sein")
        @Size(max = 255, message = "Beschreibung darf maximal 255 Zeichen lang sein")
        String description,

        @NotNull(message = "Tutor-ID darf nicht null sein")
        @ExistingTutorId
        Long tutorId,

        @NotNull(message = "ECTS darf nicht null sein")
        @Min(value = 1, message = "ECTS muss mindestens 1 sein")
        short ects
) {}
