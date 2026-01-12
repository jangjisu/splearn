package com.clean.splearn.application;

import com.clean.splearn.application.provided.MemberRegister;
import com.clean.splearn.application.required.EmailSender;
import com.clean.splearn.application.required.MemberRepository;
import com.clean.splearn.domain.Member;
import com.clean.splearn.domain.MemberRegisterRequest;
import com.clean.splearn.domain.PasswordEncoder;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MemberService implements MemberRegister {
    private final MemberRepository memberRepository;
    private final EmailSender emailSender;
    private final PasswordEncoder passwordEncoder;

    @Override
    public Member register(MemberRegisterRequest registerRequest) {
        // check

        Member member = Member.register(registerRequest, passwordEncoder);

        memberRepository.save(member);

        emailSender.send(member.getEmail(), "등록을 완료해주세요", "아래 링크를 클릭해서 등록을 완료해주세요");

        return member;
    }
}
