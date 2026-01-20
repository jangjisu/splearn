package com.clean.splearn.application.member.provided;

import com.clean.splearn.domain.member.Member;

/**
 * 회원을 조회 한다.
 */
public interface MemberFinder {
    Member find(Long memberId);
}
