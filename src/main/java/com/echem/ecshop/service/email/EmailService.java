package com.echem.ecshop.service.email;

import jakarta.mail.internet.MimeMessage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class EmailService implements EmailSender{

    private final JavaMailSender mailSender;

    public EmailService(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    @Value("${spring.mail.username}")
    private String mailFrom;

    @Override
    @Async
    public void send(String to, String email, String subject) {
        log.info("Method send in EmailService sending email to " + to);
        try {
            MimeMessage mimeMessage = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage);
            helper.setText(email);
            helper.setTo(to);
            helper.setSubject(subject);
            helper.setFrom(mailFrom);
            mailSender.send(mimeMessage);
            log.info("Email sent to {}", to);
        } catch (Exception e) {
            log.error("Fail to send an email",e);
//            throw new IllegalStateException("Fail to send an email");
        }
    }
}
