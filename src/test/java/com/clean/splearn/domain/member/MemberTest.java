package com.clean.splearn.domain.member;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static com.clean.splearn.domain.member.MemberFixture.createMemberRegisterRequest;
import static com.clean.splearn.domain.member.MemberFixture.createPasswordEncoder;
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

    @DisplayName("새로 만들어진 상태의 회원의 PENDING 상태이며, detail 에 등록일시가 추가된다.")
    @Test
    void registerMember() {
        // given // when // then
        assertThat(member.getStatus()).isEqualTo(MemberStatus.PENDING);
        assertThat(member.getDetail().getRegisteredAt()).isNotNull();
    }

    @DisplayName("활성화 하면 회원이 활성화 상태가 된다.")
    @Test
    void activate() {
        // given // when
        assertThat(member.getDetail().getActivatedAt()).isNull();
        member.activate();

        // then
        assertThat(member.getStatus()).isEqualTo(MemberStatus.ACTIVE);
        assertThat(member.getDetail().getActivatedAt()).isNotNull();
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
        assertThat(member.getDetail().getDeactivatedAt()).isNull();
        member.deactivate();

        // then
        assertThat(member.getStatus()).isEqualTo(MemberStatus.DEACTIVATED);
        assertThat(member.getDetail().getDeactivatedAt()).isNotNull();
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
        assertThat(member.verifyPassword("passwordSecret", passwordEncoder)).isTrue();
        assertThat(member.verifyPassword("hello", passwordEncoder)).isFalse();
    }

    @DisplayName("비밀번호를 변경할 수 있다")
    @Test
    void changePassword() {
        // given // when
        member.changePassword("verySecret", passwordEncoder);

        // then
        assertThat(member.verifyPassword("verySecret", passwordEncoder)).isTrue();
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

    @DisplayName("회원의 정보와 상세정보를 업데이트한다.")
    @Test
    void updateInfo() {
        // given
        member.activate();
        // when
        var updateRequest = new MemberInfoUpdateRequest("David", "jsjang100", "자기소개");
        member.updateInfo(updateRequest);

        // then
        assertThat(member.getNickname()).isEqualTo(updateRequest.nickname());
        assertThat(member.getDetail().getProfile().address()).isEqualTo(updateRequest.profileAddress());
        assertThat(member.getDetail().getIntroduction()).isEqualTo(updateRequest.introduction());
    }

    @DisplayName("활성화된 멤버만 정보를 수정할 수 있다.")
    @Test
    void updateInfoFail() {
        // given // when // then
        var updateRequest = new MemberInfoUpdateRequest("David", "jsjang100", "자기소개");
        assertThatThrownBy(() -> member.updateInfo(updateRequest))
                .isInstanceOf(IllegalStateException.class);
    }

}