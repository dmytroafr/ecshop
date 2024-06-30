package com.echem.ecshop.dto;

import com.echem.ecshop.domain.Role;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserDTO {
    public Long id;
    public String username;
    public String firstName;
    public String lastName;
    public String email;
    public String phone;
    public Role role;
    public boolean accountNonExpired;
    public boolean accountNonLocked;
    public boolean credentialsNonExpired;
    public boolean enabled;
    public LocalDateTime created;
}
