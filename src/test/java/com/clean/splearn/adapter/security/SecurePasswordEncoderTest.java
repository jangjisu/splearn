package com.clean.splearn.adapter.security;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class SecurePasswordEncoderTest {
    @DisplayName("암호화된 패스워드를 matches 메소드를 통해 비교할 수 있다.")
    @Test
    void securePasswordEncoder() {
        // given
        SecurePasswordEncoder securePasswordEncoder = new SecurePasswordEncoder();

        // when
        String passwordHash = securePasswordEncoder.encode("secret");

        // then
        assertThat(securePasswordEncoder.matches("secret", passwordHash)).isTrue();
        assertThat(securePasswordEncoder.matches("wrong", passwordHash)).isFalse();
    }

}