package com.clean.splearn.adapter.webapi;

import com.clean.splearn.adapter.webapi.dto.MemberRegisterResponse;
import com.clean.splearn.application.member.provided.MemberRegister;
import com.clean.splearn.domain.member.Member;
import com.clean.splearn.domain.member.MemberRegisterRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class MemberApi {
    private final MemberRegister memberRegister;

    @PostMapping("/api/members")
    public MemberRegisterResponse register(@RequestBody @Valid MemberRegisterRequest request) {
        Member register = memberRegister.register(request);

        return MemberRegisterResponse.of(register);
    }
}
