package com.echem.ecshop.dto;


public record AuthenticationRequest(
        String username,
        String password
) {
}
