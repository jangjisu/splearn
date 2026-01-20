package com.clean.splearn.adapter.integration;

import com.clean.splearn.application.member.required.EmailSender;
import com.clean.splearn.domain.shared.Email;
import org.springframework.stereotype.Component;

@Component
public class DummyEmailSender implements EmailSender {
    @Override
    public void send(Email email, String subject, String body) {
        System.out.println("DummyEmailSender send email: " + email);
    }
}
