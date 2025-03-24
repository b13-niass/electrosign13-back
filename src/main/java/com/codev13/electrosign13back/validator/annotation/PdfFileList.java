package com.codev13.electrosign13back.validator.annotation;

import com.codev13.electrosign13back.validator.PdfFileListValidator;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.FIELD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = PdfFileListValidator.class)
public @interface PdfFileList {
    String message() default "Tous les fichiers doivent être des PDF valides";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
