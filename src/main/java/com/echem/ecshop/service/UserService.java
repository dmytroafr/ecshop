package com.echem.ecshop.service;
import com.echem.ecshop.domain.User;
import com.echem.ecshop.dto.UserDTO;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.util.List;

public interface UserService  extends UserDetailsService {
	boolean save (UserDTO userDTO);
	List<UserDTO> getAll();
	User findByName(String name);
	void updateProfile(UserDTO dto);
}