package com.echem.ecshop.service.registration.token;

import com.echem.ecshop.dao.TokenRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
@AllArgsConstructor
public class ConfirmationTokenService {
    private final TokenRepository tokenRepository;

    public void saveConfirmationToken (ConfirmationToken token){
        tokenRepository.save(token);
    }

    public Optional<ConfirmationToken> getToken (String token){
        return tokenRepository.findByToken(token);
    }
    public void setConfirmed(String token){
        ConfirmationToken confirmationToken = tokenRepository.findByToken(token).orElseThrow(
                () -> new IllegalStateException("Token not found")
        );
        confirmationToken.setConfirmedAt(LocalDateTime.now());
    }
}
