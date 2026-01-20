package com.clean.splearn.application.member.provided;

import com.clean.splearn.SplearnTestConfiguration;
import com.clean.splearn.domain.member.Member;
import com.clean.splearn.domain.member.MemberFixture;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SpringBootTest
@Transactional
@Import(SplearnTestConfiguration.class)
record MemberFinderTest(MemberFinder memberFinder, MemberRegister memberRegister, EntityManager entityManager) {
    @DisplayName("회원을 조회한다.")
    @Test
    void find() {
        // given
        Member member = memberRegister.register(MemberFixture.createMemberRegisterRequest());
        entityManager.flush();
        entityManager.clear();

        // when
        Member findMember = memberFinder.find(member.getId());

        // then
        assertThat(member.getId()).isEqualTo(findMember.getId());
    }

    @DisplayName("없는 ID 회원 조회시 에러가 발생한다")
    @Test
    void findFail() {
        // given // when // then
        assertThatThrownBy(() -> memberFinder.find(999L))
                .isInstanceOf(IllegalArgumentException.class);
    }
}