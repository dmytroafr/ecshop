package com.echem.ecshop.service.registration;

import com.echem.ecshop.dto.RegistrationRequest;

public interface RegistrationService {
    String register(RegistrationRequest request);
    void confirmToken (String token);

}
