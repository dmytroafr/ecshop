package com.echem.ecshop.dto;

import jakarta.validation.constraints.Email;

public record RegistrationRequest(
        String username,
        String password,
        @Email
        String email) {
}
