package com.echem.ecshop.email;

import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;

@Service
@AllArgsConstructor
public class EmailService implements EmailSender{

    private static final Logger LOGGER = LoggerFactory.getLogger(EmailService.class);
    private final JavaMailSender mailSender;

    @Override
    @Async
    public void send(String to, String email) {
        try {
            MimeMessage mimeMessage = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage);
            helper.setText(email);
            helper.setTo(to);
            helper.setSubject("Please confirm your email");
            helper.setFrom("java@e-chem.com.ua");
            mailSender.send(mimeMessage);

        } catch (MessagingException e) {
            LOGGER.error("fail to send an email",e);
            throw new IllegalStateException("fail to send an email");
        }
    }
}
