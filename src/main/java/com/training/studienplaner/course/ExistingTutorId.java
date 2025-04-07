package com.training.studienplaner.course;

import com.training.studienplaner.assignment.ExistingAssignmentIdValidator;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = ExistingTutorIdValidator.class)
public @interface ExistingTutorId {
    String message() default "Tutor mit diesem id existiert nicht";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
