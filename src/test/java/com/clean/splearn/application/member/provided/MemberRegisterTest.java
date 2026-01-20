package com.clean.splearn.application.member.provided;

import com.clean.splearn.SplearnTestConfiguration;
import com.clean.splearn.domain.member.*;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import jakarta.validation.ConstraintViolationException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;

import static org.assertj.core.api.Assertions.*;

@SpringBootTest
@Transactional
@Import(SplearnTestConfiguration.class)
record MemberRegisterTest(MemberRegister memberRegister, EntityManager entityManager) {

    @DisplayName("회원을 등록한다")
    @Test
    void register() {
        Member member = memberRegister.register(MemberFixture.createMemberRegisterRequest());

        assertThat(member.getId()).isNotNull();
        assertThat(member.getStatus()).isEqualTo(MemberStatus.PENDING);
    }

    @DisplayName("동일한 이메일 가진 회원은 등록할 수 없다.")
    @Test
    void duplicateEmailFail() {
        memberRegister.register(MemberFixture.createMemberRegisterRequest());

        assertThatThrownBy(() -> memberRegister.register(MemberFixture.createMemberRegisterRequest()))
                .isInstanceOf(DuplicateEmailException.class);
    }

    @DisplayName("회원 생성에 실패한다.")
    @Test
    void memberRegisterRequestFail() {

        extracted(new MemberRegisterRequest("jsjangdv@gmail.com", "Toby", "secret"));
        extracted(new MemberRegisterRequest("jsjangdv@gmail.com", "David", "secret"));
        extracted(new MemberRegisterRequest("jsjangdv@gmail.com", "DavidTobyDavidTobyDavid", "secret"));
        extracted(new MemberRegisterRequest("jsjangdv", "DavidTobyDavidTobyDavid", "secret"));
    }

    private void extracted(MemberRegisterRequest invalid) {
        assertThatThrownBy(() -> memberRegister.register(invalid))
            .isInstanceOf(ConstraintViolationException.class);
    }

    @DisplayName("회원을 활성화 한다.")
    @Test
    void activate() {
        // given
        Member member = memberRegister.register(MemberFixture.createMemberRegisterRequest());
        entityManager.flush();
        entityManager.clear();

        // when
        member = memberRegister.activate(member.getId());
        entityManager.flush();

        // then
        assertThat(member.isActive()).isTrue();
    }
}