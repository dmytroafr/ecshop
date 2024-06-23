package com.echem.ecshop.service.token;

import com.echem.ecshop.domain.ConfirmationToken;

import java.util.Optional;

public interface ConfirmationTokenService {

    Optional<ConfirmationToken> getToken (String token);
    void saveConfirmationToken (ConfirmationToken token);
    void setConfirmed(String token);
}
