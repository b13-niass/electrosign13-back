package com.codev13.electrosign13back.validator;

import com.codev13.electrosign13back.validator.annotation.ValidMultiPattern;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.stereotype.Component;

import java.util.regex.Pattern;

@Component
public class MultiPatternValidator implements ConstraintValidator<ValidMultiPattern, String> {

    private Pattern[] patterns;

    @Override
    public void initialize(ValidMultiPattern constraintAnnotation) {
        String[] regexArray = constraintAnnotation.patterns();
        patterns = new Pattern[regexArray.length];

        for (int i = 0; i < regexArray.length; i++) {
            patterns[i] = Pattern.compile(regexArray[i]);
        }
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if (value == null) {
            return true; // Accepter les valeurs null par défaut
        }

        for (Pattern pattern : patterns) {
            if (pattern.matcher(value).matches()) {
                return true;
            }
        }

        return false;
    }
}
