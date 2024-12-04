package com.example.elevendash.global.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;


@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = BusinessHoursValidator.class)

public @interface ValidBusinessHours {
    String message() default "영업 종료 시간은 시작 시간보다 늦어야 합니다";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
