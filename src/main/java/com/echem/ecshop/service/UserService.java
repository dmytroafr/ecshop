package com.echem.ecshop.service;

import com.echem.ecshop.domain.User;
import com.echem.ecshop.dto.UserDTO;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.util.List;
import java.util.Optional;

public interface UserService  extends UserDetailsService {

    List<User> getAllUsers();

    User findByEmail(String email);

    User findByName(String username);
    String signUpUser(User user);
    void enableUser (String email);
}
