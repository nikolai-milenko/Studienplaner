package com.training.studienplaner.assignment;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = ExistingAssignmentIdValidator.class)
public @interface ExistingAssignmentId {
    String message() default "Assignment existiert nicht";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}

