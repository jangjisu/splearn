package com.clean.splearn.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class MemberTest {
    @DisplayName("새로 만들어진 상태의 멤버는 PENDING 상태이다")
    @Test
    void createMember() {
        // given
        var member = new Member("jsjang@splearn.app", "david", "secret");

        // when // then
        assertThat(member.getStatus()).isEqualTo(MemberStatus.PENDING);
    }

    @DisplayName("만들어지는 멤버의 Email은 Null 일 수 없다")
    @Test
    void constructorNullCheck() {
        // given // when // then
        assertThatThrownBy(() -> new Member(null, "david", "secret"))
                .isInstanceOf(NullPointerException.class);
    }

    @DisplayName("활성화 하면 멤버가 활성화 상태가 된다.")
    @Test
    void activate() {
        // given
        var member = new Member("jsjang@splearn.app", "david", "secret");

        // when
        member.activate();

        // then
        assertThat(member.getStatus()).isEqualTo(MemberStatus.ACTIVE);
    }

    @DisplayName("대기 상태가 아닌 상황에서 활성화 할 경우 에러가 발생한다")
    @Test
    void activateFail() {
        // given
        var member = new Member("jsjang@splearn.app", "david", "secret");
        member.activate();

        // when // then
        assertThatThrownBy(member::activate)
                .isInstanceOf(IllegalStateException.class);
    }

    @DisplayName("비활성화 상태로 변경할 수 있다")
    @Test
    void deactivate() {
        // given
        var member = new Member("jsjang@splearn.app", "david", "secret");
        member.activate();

        // when
        member.deactivate();

        // then
        assertThat(member.getStatus()).isEqualTo(MemberStatus.DEACTIVATED);
    }

    @DisplayName("활성화 상태가 아닌 상황에서 비활성화 할 경우 에러가 발생한다")
    @Test
    void deactivateFail() {
        // given
        var member = new Member("jsjang@splearn.app", "david", "secret");

        // when // then
        assertThatThrownBy(member::deactivate)
                .isInstanceOf(IllegalStateException.class);

        member.activate();
        member.deactivate();

        assertThatThrownBy(member::deactivate)
                .isInstanceOf(IllegalStateException.class);
    }

}