package com.echem.ecshop.service.email;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
public class EmailService implements EmailSender{

    private static final Logger LOGGER = LoggerFactory.getLogger(EmailService.class);
    private final JavaMailSender mailSender;


    public EmailService(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    @Value("${spring.mail.username}")
    private String mailFrom;

    @Override
    @Async
    public void send(String to, String email, String subject) {
        try {
            MimeMessage mimeMessage = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage);
            helper.setText(email);
            helper.setTo(to);
            helper.setSubject(subject);
            helper.setFrom(mailFrom);
            mailSender.send(mimeMessage);

        } catch (MessagingException e) {
            LOGGER.error("fail to send an email",e);
            throw new IllegalStateException("fail to send an email");
        }
    }
}
