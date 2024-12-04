package com.example.elevendash.global.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.time.LocalTime;

public class BusinessHoursValidator implements ConstraintValidator<ValidBusinessHours, LocalTime> {
    /**
     * TODO 구현 고민중
     * @param localTime
     * @param constraintValidatorContext
     * @return
     */
    @Override
    public boolean isValid(LocalTime localTime, ConstraintValidatorContext constraintValidatorContext) {
        return false;
    }
}
