package com.echem.ecshop.service.registration;

import com.echem.ecshop.domain.Role;
import com.echem.ecshop.domain.User;
import com.echem.ecshop.dto.RegistrationRequest;
import com.echem.ecshop.service.email.EmailSender;
import com.echem.ecshop.service.registration.token.ConfirmationToken;
import com.echem.ecshop.service.registration.token.ConfirmationTokenService;
import com.echem.ecshop.service.user.UserService;
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

    @Transactional
    @Override
    public String register(RegistrationRequest request) {
        String token = userService.signUpUser(new User(
                request.userName(),
                request.password(),
                request.email(),
                Role.CLIENT
        ));

        String link = serverHost + "registration/confirm?token=" + token;
        String message = String.format("Привіт, " + request.userName() +
                ", перейдіть за наступним посиланням для підтвердження адреси вашої пошти:" +
                "\n%s\n\nДякуємо,\nЗ повагою,\nКоманда Екохім", link);
        emailSender.send(request.email(), message, "Підтвердження реєстрації>");
        return token;
    }
    @Transactional
    @Override
    public String confirmToken(String token) {
        ConfirmationToken confirmationToken = tokenService.getToken(token).orElseThrow(
                ()->new IllegalStateException("token not found"));

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
