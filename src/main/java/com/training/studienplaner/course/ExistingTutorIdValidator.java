package com.training.studienplaner.course;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ExistingTutorIdValidator implements ConstraintValidator<ExistingTutorId, Long> {
    private final CourseRepository courseRepository;

    @Override
    public boolean isValid(Long tutorId, ConstraintValidatorContext context){
        if (tutorId == null) {
            return false;
        }
        return courseRepository.existsById(tutorId);
    }
}
