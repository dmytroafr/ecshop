package com.echem.ecshop.dto;

import jakarta.validation.constraints.Email;

public record RegistrationRequest(

        String userName,
        String password,
        @Email
        String email) {
}
