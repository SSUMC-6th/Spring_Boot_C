package com.example.umc.validation.validator;

import com.example.umc.domain.maaping.error.status.ErrorStatus;
import com.example.umc.repository.StoreRepository;
import com.example.umc.validation.annotation.ExistStore;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class StoreExistValidator implements ConstraintValidator<ExistStore, Long> {

        private final StoreRepository storeRepository;

        @Override
        public void initialize(ExistStore constraintAnnotation) {
            ConstraintValidator.super.initialize(constraintAnnotation);
        }

        @Override
        public boolean isValid(Long value, ConstraintValidatorContext context) {
            boolean isValid = storeRepository.existsById(value);

            if (!isValid) {
                context.disableDefaultConstraintViolation();
                context.buildConstraintViolationWithTemplate(ErrorStatus.STORE_NOT_FOUND.toString()).addConstraintViolation();
            }

            return isValid;

        }
}
