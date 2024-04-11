package com.echem.ecshop.registration;

public record RegistrationRequest(
        String firstName,
        String lastName,
        String password,
        String email) {
}
