package com.example.umc.domain.Loader;

import com.example.umc.domain.Member;
import com.example.umc.domain.enums.Gender;
import com.example.umc.domain.enums.MemberStatus;
import com.example.umc.domain.enums.SocialType;
import com.example.umc.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class DataLoader implements CommandLineRunner {

    private final MemberRepository memberRepository;

    @Override
    public void run(String... args) throws Exception {
        Member member = Member.builder()
                .name("John Doe")
                .address("123 Main St")
                .specAddress("Apt 4B")
                .gender(Gender.MALE)
                .socialType(SocialType.KAKAO) // Assuming SocialType.FACEBOOK is a valid value
                .status(MemberStatus.ACTIVE)
                .email("john.doe@example.com")
                .point(100)
                .build();

        memberRepository.save(member);
    }
}
