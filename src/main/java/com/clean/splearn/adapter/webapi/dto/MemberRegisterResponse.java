package com.clean.splearn.adapter.webapi.dto;

import com.clean.splearn.domain.member.Member;

public record MemberRegisterResponse(Long memberId, String email) {
    public static MemberRegisterResponse of(Member member) {
        return new MemberRegisterResponse(member.getId(), member.getEmail().address());
    }
}
