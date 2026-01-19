package com.clean.splearn.application.provided;

import com.clean.splearn.domain.Member;

/**
 * 회원을 조회 한다.
 */
public interface MemberFinder {
    Member find(Long memberId);
}
