package com.example.elevendash.global.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import jakarta.validation.Valid;

public class PhoneNumberValidator implements ConstraintValidator<ValidPhoneNumber, String> {
    private static final String PHONE_NUMBER_REGEX = "^(01[016789]-?\\d{3,4}-?\\d{4})$\n";

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if (value == null) {
            return true;
        }
        return value.matches(PHONE_NUMBER_REGEX);
    }
}
