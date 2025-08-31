package com.echem.ecshop.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;

public record RegistrationRequest(
        @Size(min = 4, max = 20)
        String username,
        @Size(min = 8, max = 20)
        String password,
        @Email
        String email) {
}
