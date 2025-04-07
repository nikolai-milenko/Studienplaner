package com.training.studienplaner.assignment;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ExistingAssignmentIdValidator implements ConstraintValidator<ExistingAssignmentId, Long> {

    private final AssignmentRepository assignmentRepository;

    @Override
    public boolean isValid(Long assignmentId, ConstraintValidatorContext context) {
        if (assignmentId == null) {
            return false;
        }
        return assignmentRepository.existsById(assignmentId);
    }
}

