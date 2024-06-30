package com.echem.ecshop.service.token;

import com.echem.ecshop.dao.TokenRepository;
import com.echem.ecshop.domain.ConfirmationToken;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@Slf4j
@Service
public class ConfirmationTokenServiceImpl implements ConfirmationTokenService{

    private final TokenRepository tokenRepository;

    public ConfirmationTokenServiceImpl(TokenRepository tokenRepository) {
        this.tokenRepository = tokenRepository;
    }

    @Override
    public void saveConfirmationToken (ConfirmationToken token){
        log.info("Saving confirmation token");
        tokenRepository.save(token);
    }

    @Override
    public Optional<ConfirmationToken> getToken (String token){
        log.info("Retrieving confirmation token");
        return tokenRepository.findByToken(token);
    }

    @Override
    public void setConfirmed(String token){
        ConfirmationToken confirmationToken = getToken(token)
                .orElseThrow(() -> {
                    log.error("Token not found");
                    return new IllegalStateException("Token not found");
                }
        );
        confirmationToken.setConfirmedAt(LocalDateTime.now());
        log.info("Token was set Confirmed");
    }
}
