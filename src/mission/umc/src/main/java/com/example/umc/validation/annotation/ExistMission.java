package com.example.umc.validation.annotation;

import com.example.umc.validation.validator.MissionExistValidation;
import com.example.umc.validation.validator.RegionExistValidator;
import jakarta.validation.Constraint;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Constraint(validatedBy = MissionExistValidation.class)
@Target({ElementType.METHOD, ElementType.FIELD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
public @interface ExistMission {

    String message() default "해당하는 미션이 존재하지 않습니다.";

    Class<?>[] groups() default {};

    Class<?>[] payload() default {};
}
