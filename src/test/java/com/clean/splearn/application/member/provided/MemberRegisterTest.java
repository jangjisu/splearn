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

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

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

        checkValidation(new MemberRegisterRequest("jsjangdv@gmail.com", "Toby", "secret"));
        checkValidation(new MemberRegisterRequest("jsjangdv@gmail.com", "David", "secret"));
        checkValidation(new MemberRegisterRequest("jsjangdv@gmail.com", "DavidTobyDavidTobyDavid", "secret"));
        checkValidation(new MemberRegisterRequest("jsjangdv", "DavidTobyDavidTobyDavid", "secret"));
    }

    private void checkValidation(MemberRegisterRequest invalid) {
        assertThatThrownBy(() -> memberRegister.register(invalid))
            .isInstanceOf(ConstraintViolationException.class);
    }

    @DisplayName("회원을 활성화 한다.")
    @Test
    void activate() {
        // given
        Member member = registerMember();

        // when
        member = memberRegister.activate(member.getId());
        entityManager.flush();

        // then
        assertThat(member.isActive()).isTrue();
    }

    @DisplayName("회원을 비활성화 한다.")
    @Test
    void deactivate() {
        // given
        Member member = registerMember();

        member = memberRegister.activate(member.getId());
        entityManager.flush();
        entityManager.clear();

        // when
        member = memberRegister.deactivate(member.getId());

        entityManager.flush();

        // then
        assertThat(member.isActive()).isFalse();
        assertThat(member.getDetail().getDeactivatedAt()).isNotNull();
    }

    @DisplayName("회원 정보를 업데이트 한다.")
    @Test
    void updateInfo() {
        // given
        Member member = registerMember();

        member = memberRegister.activate(member.getId());
        entityManager.flush();
        entityManager.clear();

        // when
        MemberInfoUpdateRequest memberInfoUpdateRequest = new MemberInfoUpdateRequest("David", "a1b1", "방가방가");
        member = memberRegister.updateInfo(member.getId(), memberInfoUpdateRequest);

        // then
        assertThat(member.getNickname()).isEqualTo(memberInfoUpdateRequest.nickname());
        assertThat(member.getDetail().getProfile().address()).isEqualTo(memberInfoUpdateRequest.profileAddress());
        assertThat(member.getDetail().getIntroduction()).isEqualTo(memberInfoUpdateRequest.introduction());
    }

    @DisplayName("같은 프로필로 업데이트 할 수 없다.")
    @Test
    void updateInfoFail() {
        Member member = registerMember();
        memberRegister.activate(member.getId());
        MemberInfoUpdateRequest memberInfoUpdateRequest = new MemberInfoUpdateRequest("David", "a1b1", "방가방가");
        memberRegister.updateInfo(member.getId(), memberInfoUpdateRequest);

        entityManager.flush();
        entityManager.clear();

        Member member2 = registerMember("toby2@splearn.app");
        memberRegister.activate(member2.getId());
        entityManager.flush();
        entityManager.clear();

        Member member3 = registerMember("toby23@splearn.app");
        memberRegister.activate(member3.getId());
        MemberInfoUpdateRequest memberInfoUpdateRequest3 = new MemberInfoUpdateRequest("David", "axsbx", "방가방가");
        memberRegister.updateInfo(member3.getId(), memberInfoUpdateRequest3);

        entityManager.flush();
        entityManager.clear();

        // member2는 기존의 member 와 같은 프로필 주소를 사용할 수 없다.
        assertThatThrownBy(() -> memberRegister.updateInfo(member2.getId(), memberInfoUpdateRequest))
            .isInstanceOf(DuplicateProfileException.class);

        //다른 프로필 주소로는 변경 가능
        memberRegister.updateInfo(member2.getId(), new MemberInfoUpdateRequest("David", "a1b2", "방가방가"));

        //기존 프로필 주소를 바꾸는 것도 가능
        memberRegister.updateInfo(member.getId(), new MemberInfoUpdateRequest("David", "a1b1", "방가방가"));

        // 프로필 주소 중복은 허용하지 않음
        assertThatThrownBy(() -> memberRegister.updateInfo(member.getId(), new MemberInfoUpdateRequest("David", "a1b2", "방가방가")))
                .isInstanceOf(DuplicateProfileException.class);
    }

    private Member registerMember() {
        Member member = memberRegister.register(MemberFixture.createMemberRegisterRequest());
        entityManager.flush();
        entityManager.clear();
        return member;
    }

    private Member registerMember(String email) {
        Member member = memberRegister.register(MemberFixture.createMemberRegisterRequest(email));
        entityManager.flush();
        entityManager.clear();
        return member;
    }
}