package com.echem.ecshop.dto;

import com.echem.ecshop.domain.Role;

public record UserDTO
        (Long id,
         String username,
         String email,
         Role role){
}

