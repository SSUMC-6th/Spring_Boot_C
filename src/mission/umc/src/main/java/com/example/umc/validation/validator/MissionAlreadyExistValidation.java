package com.example.umc.validation.validator;

import com.example.umc.domain.Member;
import com.example.umc.domain.maaping.error.status.ErrorStatus;
import com.example.umc.repository.MemberRepository;
import com.example.umc.validation.annotation.AlreadyExistMission;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class MissionAlreadyExistValidation implements ConstraintValidator<AlreadyExistMission, Long> {

        private final MemberRepository memberRepository;

        @Override
        public void initialize(AlreadyExistMission constraintAnnotation) {
            ConstraintValidator.super.initialize(constraintAnnotation);
        }

        @Override
        public boolean isValid(Long value, ConstraintValidatorContext context) {

            Member member = memberRepository.findById(value).get();
            boolean missionExist = member.getMemberMissionList().stream().anyMatch(memberMission -> memberMission.getMission().getId().equals(value));

            if (missionExist) {
                context.disableDefaultConstraintViolation();
                context.buildConstraintViolationWithTemplate(ErrorStatus.MISSION_ALREADY_EXIST.toString()).addConstraintViolation();
                return false;
            }

            return true;
        }
}
