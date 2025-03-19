package com.codev13.electrosign13back.validator.annotation;

import com.codev13.electrosign13back.validator.MultiPatternValidator;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = MultiPatternValidator.class)
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidMultiPattern {
    String message() default "La valeur ne correspond à aucun des formats attendus";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};

    String[] patterns(); // Liste des regex à vérifier
}
