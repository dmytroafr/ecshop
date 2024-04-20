package com.echem.ecshop.dto;

import com.echem.ecshop.domain.Bucket;
import lombok.Builder;

import java.util.List;

@Builder
public record UserDTO (
        String username,
        String password,
        String email,
        String phone,
        List<Bucket> bucket){
}
