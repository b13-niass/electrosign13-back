package com.codev13.electrosign13back.validator.annotation;

import com.codev13.electrosign13back.validator.ExistsListValidator;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = ExistsListValidator.class)
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface ExistsList {
    String message() default "Un ou plusieurs éléments de la liste n'existent pas dans la base de données";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};

    String field();
    Class<?> entity();
}

