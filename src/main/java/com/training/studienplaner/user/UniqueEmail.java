package com.training.studienplaner.user;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = UniqueEmailValidator.class)
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface UniqueEmail {
    String message() default "Diese E-Mail Adresse wird bereits verwendet";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
