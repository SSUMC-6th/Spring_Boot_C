package com.example.umc.validation.annotation;

import com.example.umc.validation.validator.MissionAlreadyExistValidation;
import jakarta.validation.Constraint;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Constraint(validatedBy = MissionAlreadyExistValidation.class)
@Target({ElementType.METHOD, ElementType.FIELD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
public @interface AlreadyExistMission {

    String message() default "해당하는 미션은 이미 진행 중입니다.";

    Class<?>[] groups() default {};

    Class<?>[] payload() default {};
}
