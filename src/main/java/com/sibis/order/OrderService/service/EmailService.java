package com.sibis.order.OrderService.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class EmailService {

    private final JavaMailSender javaMailSender;

    public EmailService(JavaMailSender javaMailSender) {
        this.javaMailSender = javaMailSender;
    }

    private void logEmail(String from, String to, String subject, String body) {
        log.info(String.format("From: %s", from));
        log.info(String.format("To: %s", to));
        log.info(String.format("Subject: %s", subject));
        log.info(String.format("body: %s", body));
    }

    @Async
    public void sendEmail(String from, String to, String subject, String body) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(from);
        message.setTo(to);
        message.setSubject(subject);
        message.setText(body);

        try {
            javaMailSender.send(message);
            this.logEmail(from, to, subject, body);

        } catch (Exception e) {
            log.error(e.getMessage());
        }
    }
}