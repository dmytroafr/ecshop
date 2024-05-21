package com.echem.ecshop.registration;

import com.echem.ecshop.dto.RegistrationRequest;

public interface RegistrationService {
    String register(RegistrationRequest request);
    String confirmToken (String token);

}
