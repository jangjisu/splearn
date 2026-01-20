package com.clean.splearn.domain.shared;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class EmailTest {
    @DisplayName("이메일 동등성 확인")
    @Test
    void equality() {
        // given // when
        var email1 = new Email("jsjangdv@gmail.com");
        var email2 = new Email("jsjangdv@gmail.com");

        // then
        assertThat(email1).isEqualTo(email2);
    }

}
