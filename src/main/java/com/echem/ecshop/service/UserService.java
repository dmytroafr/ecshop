package com.echem.ecshop.service;

import com.echem.ecshop.dto.UserDTO;
import org.springframework.security.core.userdetails.UserDetailsService;

public interface UserService  extends UserDetailsService {
	boolean save (UserDTO userDTO);
}
