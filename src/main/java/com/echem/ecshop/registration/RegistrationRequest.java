package com.echem.ecshop.registration;

public record RegistrationRequest(
        String userName,
        String password,
        String email) {
}
