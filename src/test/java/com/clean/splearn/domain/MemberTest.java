package com.clean.splearn.domain;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static com.clean.splearn.domain.MemberFixture.createMemberRegisterRequest;
import static com.clean.splearn.domain.MemberFixture.createPasswordEncoder;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class MemberTest {
    Member member;
    PasswordEncoder passwordEncoder;

    @BeforeEach
    void setUp() {
        this.passwordEncoder = createPasswordEncoder();
        member = Member.register(createMemberRegisterRequest(), passwordEncoder);
    }

    @DisplayName("새로 만들어진 상태의 회원의 PENDING 상태이다")
    @Test
    void registerMember() {
        // given // when // then
        assertThat(member.getStatus()).isEqualTo(MemberStatus.PENDING);
    }

    @DisplayName("활성화 하면 회원이 활성화 상태가 된다.")
    @Test
    void activate() {
        // given // when
        member.activate();

        // then
        assertThat(member.getStatus()).isEqualTo(MemberStatus.ACTIVE);
    }

    @DisplayName("대기 상태가 아닌 상황에서 활성화 할 경우 에러가 발생한다")
    @Test
    void activateFail() {
        // given
        member.activate();

        // when // then
        assertThatThrownBy(member::activate)
                .isInstanceOf(IllegalStateException.class);
    }

    @DisplayName("비활성화 상태로 변경할 수 있다")
    @Test
    void deactivate() {
        // given
        member.activate();

        // when
        member.deactivate();

        // then
        assertThat(member.getStatus()).isEqualTo(MemberStatus.DEACTIVATED);
    }

    @DisplayName("활성화 상태가 아닌 상황에서 비활성화 할 경우 에러가 발생한다")
    @Test
    void deactivateFail() {
        // given // when // then
        assertThatThrownBy(member::deactivate)
                .isInstanceOf(IllegalStateException.class);

        member.activate();
        member.deactivate();

        assertThatThrownBy(member::deactivate)
                .isInstanceOf(IllegalStateException.class);
    }

    @DisplayName("비밀번호를 검증한다")
    @Test
    void verifyPassword() {
        // given // when // then
        assertThat(member.verifyPassword("secret", passwordEncoder)).isTrue();
        assertThat(member.verifyPassword("hello", passwordEncoder)).isFalse();
    }

    @DisplayName("닉네임을 변경할 수 있다")
    @Test
    void changeNickName() {
        // given
        assertThat(member.getNickname()).isEqualTo("david");
        // when
        member.changeNickname("charlie");

        // then
        assertThat(member.getNickname()).isEqualTo("charlie");
    }

    @DisplayName("비밀번호를 변경할 수 있다")
    @Test
    void changePassword() {
        // given // when
        member.changePassword("verysecret", passwordEncoder);

        // then
        assertThat(member.verifyPassword("verysecret", passwordEncoder)).isTrue();
    }

    @DisplayName("회원의 활성화 여부를 판단할 수 있다")
    @Test
    void isActive() {
        // given // when // then
        assertThat(member.isActive()).isFalse();

        member.activate();
        assertThat(member.isActive()).isTrue();

        member.deactivate();
        assertThat(member.isActive()).isFalse();
    }

    @DisplayName("이메일 형식이 다르면 예외를 터트린다")
    @Test
    void invalidEmail() {
        // given // when // then
        assertThatThrownBy(() -> Member.register(createMemberRegisterRequest("invalid email"), passwordEncoder))
                .isInstanceOf(IllegalArgumentException.class);

        Member.register(createMemberRegisterRequest(), passwordEncoder);
    }

}