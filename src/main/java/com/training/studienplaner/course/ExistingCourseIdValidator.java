package com.training.studienplaner.course;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ExistingCourseIdValidator implements ConstraintValidator<ExistingCourseId, Long> {
    private final CourseRepository courseRepository;

    @Override
    public boolean isValid(Long courseId, ConstraintValidatorContext context){
        if (courseId == null) {
            return false;
        }
        return courseRepository.existsById(courseId);
    }
}
