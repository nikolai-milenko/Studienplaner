package com.training.studienplaner.course;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = ExistingCourseIdValidator.class)
public @interface ExistingCourseId {
    String message() default "Kurs mit dieser ID existiert nicht";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
