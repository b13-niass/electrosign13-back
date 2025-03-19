package com.codev13.electrosign13back.validator.annotation;

import com.codev13.electrosign13back.validator.ExistsValidator;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = ExistsValidator.class)
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface Exists {
    String message() default "La valeur doit exister dans la base de données";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};

    String field();
    Class<?> entity();
}
