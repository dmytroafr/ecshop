package com.echem.ecshop.service.user;

import com.echem.ecshop.domain.User;
import com.echem.ecshop.dto.RegistrationRequest;
import com.echem.ecshop.dto.UserDTO;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.List;
import java.util.Map;

public interface UserService  extends UserDetailsService {

    UserDetails loadUserByUsername(String username) throws UsernameNotFoundException;

    User getUserByUsername (String username);

    UserDTO getUserDTOByUserName(String username);
    UserDTO getUserDTOByEmail(String email);

    String signUpUser(RegistrationRequest request);
    void enableUser (String email);

    Map<String,Object> getUserDetailsMap (String username);
    List<UserDTO> getUsers();
}
