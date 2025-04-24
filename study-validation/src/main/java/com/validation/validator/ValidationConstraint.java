package com.validation.validator;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = ValidationValidator.class)
public @interface ValidationConstraint {

    String message() default "not valid";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

    String typeStringValue();

    String idStringValue();
}
