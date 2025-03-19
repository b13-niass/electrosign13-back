package com.codev13.electrosign13back.validator;

import com.codev13.electrosign13back.validator.annotation.ValidPhoneNumber;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class PhoneNumberValidator implements ConstraintValidator<ValidPhoneNumber, String> {

    @Override
    public boolean isValid(String phoneNumber, ConstraintValidatorContext context) {
        if (phoneNumber == null) {
            return false;
        }

        return phoneNumber.matches("^(77|76|70|78)\\d{7}$");
    }
}
