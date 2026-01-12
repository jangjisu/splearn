package com.clean.splearn.application.required;

import com.clean.splearn.domain.Member;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.dao.DataIntegrityViolationException;

import static com.clean.splearn.domain.MemberFixture.createMemberRegisterRequest;
import static com.clean.splearn.domain.MemberFixture.createPasswordEncoder;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@DataJpaTest
class MemberRepositoryTest {
    @Autowired
    MemberRepository memberRepository;

    @Autowired
    EntityManager entityManager;

    @DisplayName("멤버를 생성한다.")
    @Test
    void createMember() {
        // given
        Member member = Member.register(createMemberRegisterRequest(), createPasswordEncoder());

        // when
        Member savedMember = memberRepository.save(member);
        entityManager.flush();

        // then
        assertNotNull(savedMember.getId());
    }

    @DisplayName("같은 이메일을 가진 유저를 생성할 경우 실패한다.")
    @Test
    void duplicateEmailFail() {
        // given
        Member member = Member.register(createMemberRegisterRequest(), createPasswordEncoder());
        memberRepository.save(member);

        // when
        Member member2 = Member.register(createMemberRegisterRequest(), createPasswordEncoder());

        // then
        assertThatThrownBy(() -> memberRepository.save(member2))
                .isInstanceOf(DataIntegrityViolationException.class);

    }
}