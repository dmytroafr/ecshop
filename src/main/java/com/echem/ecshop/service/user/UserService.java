package com.echem.ecshop.service.user;

import com.echem.ecshop.domain.User;
import com.echem.ecshop.dto.UserDTO;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.util.List;

public interface UserService  extends UserDetailsService {

    List<User> getAllUsers();
    UserDTO findByUserName(String username);
    String signUpUser(User user);
    User getRefById(Long id);
    void enableUser (String email);
    void save(User user);
}
