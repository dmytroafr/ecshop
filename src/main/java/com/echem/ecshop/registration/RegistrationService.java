package com.echem.ecshop.registration;

public interface RegistrationService {
    String register(RegistrationRequest request);
    String confirmToken (String token);

}
