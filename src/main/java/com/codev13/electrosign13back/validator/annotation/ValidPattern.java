package com.codev13.electrosign13back.validator.annotation;

import com.codev13.electrosign13back.validator.PatternValidator;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = PatternValidator.class)
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidPattern {
    String message() default "La valeur ne correspond pas au format attendu";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
    String customPattern() default "";
}