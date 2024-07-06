package com.echem.ecshop.dto;

import com.echem.ecshop.domain.Role;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

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

    public Map<String, String> getMap(){
        Map<String, String> map = new HashMap<>();
        String empty = "";
        map.put("firstName", firstName!=null?firstName:empty);
        map.put("lastName", lastName!=null?lastName:empty);
        map.put("phone", phone!=null?phone:empty);
        map.put("email", email);
        map.put("role", role.toString());
        map.put("accountNonExpired", String.valueOf(accountNonExpired));
        map.put("accountNonLocked", String.valueOf(accountNonLocked));
        map.put("credentialsNonExpired", String.valueOf(credentialsNonExpired));
        map.put("enabled", String.valueOf(enabled));
        map.put("created", String.valueOf(created));
        return map;
    }


}
