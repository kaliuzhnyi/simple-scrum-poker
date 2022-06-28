package com.simplescrumpoker.service;

import lombok.RequiredArgsConstructor;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class EmailService {

    private final JavaMailSender mailSender;

    public void sendMail(String to, String subject, String text) {
        var message = new SimpleMailMessage();
        message.setTo(to);
        message.setSubject(subject);
        message.setText(text);
        mailSender.send(message);
    }

    public void sendRemindPasswordMail(String to, UUID uuid) {
        var link = "http://localhost:9090/users/password/set/%s".formatted(uuid.toString());
        var subject = "Remind password";
        var text = "If you want to set new password go to link: %s".formatted(link);
        sendMail(to, subject, text);
    }

}
