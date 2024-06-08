package com.echem.ecshop.registration;

import com.echem.ecshop.domain.Role;
import com.echem.ecshop.domain.User;
import com.echem.ecshop.dto.RegistrationRequest;
import com.echem.ecshop.email.EmailSender;
import com.echem.ecshop.registration.token.ConfirmationToken;
import com.echem.ecshop.registration.token.ConfirmationTokenService;
import com.echem.ecshop.service.UserService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
public class RegistrationServiceImpl implements RegistrationService{

    private final UserService userService;
    private final ConfirmationTokenService tokenService;
    private final EmailSender emailSender;

    @Value("${server.host}")
    private String serverHost;

    public RegistrationServiceImpl(UserService userService, ConfirmationTokenService tokenService, EmailSender emailSender) {
        this.userService = userService;
        this.tokenService = tokenService;
        this.emailSender = emailSender;
    }

    @Override
    public String register(RegistrationRequest request) {


        String token = userService.signUpUser(new User(
                request.userName(),
                request.password(),
                request.email(),
                Role.CLIENT
        ));

        String link = serverHost + "registration/confirm?token=" + token;

        emailSender.send(request.email(),String.format("hello, "+request.userName()+"!, click here: %s",link));
        return token;
    }
    @Transactional
    @Override
    public String confirmToken(String token) {
        ConfirmationToken confirmationToken = tokenService.getToken(token).orElseThrow(
                ()->new IllegalStateException("token not found")
        );

        if (confirmationToken.getConfirmedAt() != null){
            throw new IllegalStateException("email already confirmed");
        }
        LocalDateTime expiresAt = confirmationToken.getExpiresAt();
        if (expiresAt.isBefore(LocalDateTime.now())){
            throw new IllegalStateException("token expired");
        }
        tokenService.setConfirmed(token);
        userService.enableUser(confirmationToken.getUser().getEmail());
        return "confirmed";
    }
}
