package com.clean.splearn.application.member.required;

import com.clean.splearn.domain.shared.Email;

/**
 * 이메일을 발송한다
 */
public interface EmailSender {
    void send(Email email, String subject, String body);
}
