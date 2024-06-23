package com.echem.ecshop.service.token;

import com.echem.ecshop.dao.TokenRepository;
import com.echem.ecshop.domain.ConfirmationToken;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
@AllArgsConstructor
public class ConfirmationTokenServiceImpl implements ConfirmationTokenService{
    private final TokenRepository tokenRepository;

    @Override
    public void saveConfirmationToken (ConfirmationToken token){
        tokenRepository.save(token);
    }

    @Override
    public Optional<ConfirmationToken> getToken (String token){
        return tokenRepository.findByToken(token);
    }

    @Override
    public void setConfirmed(String token){
        ConfirmationToken confirmationToken = tokenRepository.findByToken(token).orElseThrow(
                () -> new IllegalStateException("Token not found")
        );
        confirmationToken.setConfirmedAt(LocalDateTime.now());
    }
}
