package com.iforddow.bizaudo.service.util_service.mail;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MailService {

    private final JavaMailSender mailSender;

    @Value("${mail.sender}")
    private String emailSender;

    public void sendMail(String to, String subject, String content) {

        SimpleMailMessage message = new SimpleMailMessage();

        message.setFrom(emailSender);
        message.setTo(to);
        message.setSubject(subject);
        message.setText(content);

        mailSender.send(message);

    }

}
