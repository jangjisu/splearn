package com.clean.splearn.adapter.integration;

import com.clean.splearn.domain.Email;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junitpioneer.jupiter.StdIo;
import org.junitpioneer.jupiter.StdOut;

import static org.assertj.core.api.Assertions.assertThat;

class DummyEmailSenderTest {
    @DisplayName("콘솔에 출력되는 부분 테스트")
    @Test
    @StdIo
    void dummyEmailSender(StdOut out) {
        // given
        DummyEmailSender dummyEmailSender = new DummyEmailSender();

        // when // then
        dummyEmailSender.send(new Email("jsjang@gmail.com"), "subject", "body");

        assertThat(out.capturedLines()[0]).isEqualTo("DummyEmailSender send email: Email[address=jsjang@gmail.com]");
    }

}