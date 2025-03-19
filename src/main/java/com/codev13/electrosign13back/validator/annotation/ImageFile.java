package com.codev13.electrosign13back.validator.annotation;

import com.codev13.electrosign13back.validator.ImageFileValidator;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.FIELD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = ImageFileValidator.class)
public @interface ImageFile {
    String message() default "Le fichier doit être une image au format JPG, JPEG ou PNG";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
