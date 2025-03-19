package com.codev13.electrosign13back.validator;

import com.codev13.electrosign13back.validator.annotation.ValidPattern;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.stereotype.Component;

import java.util.regex.Pattern;

@Component
public class PatternValidator implements ConstraintValidator<ValidPattern, String> {

    private Pattern pattern;

    @Override
    public void initialize(ValidPattern constraintAnnotation) {
        String patternString = constraintAnnotation.customPattern();
        this.pattern = Pattern.compile(patternString);
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if (value == null) {
            return true;
        }

        return pattern.matcher(value).matches();
    }
}
