package com.echem.ecshop.service.email;

public interface EmailSender {
    void send (String to, String email, String subject);
}
