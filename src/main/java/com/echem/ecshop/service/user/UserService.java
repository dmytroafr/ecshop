package com.echem.ecshop.service.user;

import com.echem.ecshop.domain.User;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.util.List;

public interface UserService  extends UserDetailsService {

    List<User> getAllUsers();
    User findByUserName(String email);
    User findByName(String username);
    String signUpUser(User user);
    void enableUser (String email);
    void save(User user);
}
