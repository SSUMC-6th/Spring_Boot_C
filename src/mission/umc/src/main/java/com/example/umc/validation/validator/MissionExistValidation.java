package com.example.umc.validation.validator;

import com.example.umc.domain.maaping.error.status.ErrorStatus;
import com.example.umc.repository.MissionRepository;
import com.example.umc.validation.annotation.ExistMission;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class MissionExistValidation implements ConstraintValidator<ExistMission, Long> {

        private final MissionRepository missionRepository;

        @Override
        public void initialize(ExistMission constraintAnnotation) {
            ConstraintValidator.super.initialize(constraintAnnotation);
        }

        @Override
        public boolean isValid(Long value, ConstraintValidatorContext context) {

            boolean isValid = missionRepository.existsById(value);

            if (!isValid) {
                context.disableDefaultConstraintViolation();
                context.buildConstraintViolationWithTemplate(ErrorStatus.MISSION_NOT_FOUND.toString()).addConstraintViolation();
            }

            return isValid;

        }
}
