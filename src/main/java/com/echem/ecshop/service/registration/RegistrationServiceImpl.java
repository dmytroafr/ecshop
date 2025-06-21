package com.echem.ecshop.service.registration;

import com.echem.ecshop.domain.ConfirmationToken;
import com.echem.ecshop.dto.RegistrationRequest;
import com.echem.ecshop.service.email.EmailSender;
import com.echem.ecshop.service.token.ConfirmationTokenService;
import com.echem.ecshop.service.user.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Slf4j
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
        log.debug("Method register was called");
        String token = userService.signUpUser(request);
        log.info("User {} was signed up successfully",request.username());
//        String link = serverHost + "registration/confirm?token=" + token;
        String link = "registration/confirm?token=" + token;

//        String message = String.format("Привіт, " + request.username() +
//                ", перейдіть за наступним посиланням для підтвердження адреси вашої пошти:" +
//                "\n%s\n\nДякуємо,\nЗ повагою,\nКоманда Екохім", link);

        String message = "Привіт, ви зареєструвалися на сайті e-chem.com.ua";

//        emailSender.send(request.email(), message, "Підтвердження реєстрації");
//        log.info("User {} was successfully registered",request.username());
        return token;
    }
    @Transactional
    @Override
    public void confirmToken(String token) {
        log.debug("Method confirmToken was called");
        ConfirmationToken confirmationToken = tokenService.getToken(token)
                .orElseThrow(()->{
                    log.error("Token {} did not found", token);
                    return new IllegalStateException("Token not found");
                });

        if (confirmationToken.getConfirmedAt() != null){
            log.error("Token {} already confirmed", confirmationToken.getToken());
            throw new IllegalStateException("Token already confirmed");
        }
        LocalDateTime expiresAt = confirmationToken.getExpiresAt();
        if (expiresAt.isBefore(LocalDateTime.now())){
            log.error("Token {} expired", confirmationToken.getToken());
            throw new IllegalStateException("Token expired");
        }
        tokenService.setConfirmed(token);
        log.info("Token {} confirmed", confirmationToken.getToken());
        userService.enableUser(confirmationToken.getUser().getUsername());
        log.info("User {} is now enabled", confirmationToken.getUser().getUsername());
    }
}
