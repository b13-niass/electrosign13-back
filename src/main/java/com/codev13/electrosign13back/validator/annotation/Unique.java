package com.codev13.electrosign13back.validator.annotation;

import com.codev13.electrosign13back.validator.UniqueValidator;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = UniqueValidator.class)
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface Unique {
    String message() default "La valeur doit être unique";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};

    String field();
    Class<?> entity();
}

//  @Unique(field = "email", entity = User.class)