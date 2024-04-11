package com.echem.ecshop.dao;

import com.echem.ecshop.domain.User;
import com.echem.ecshop.dto.UserDTO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findUserByEmail(String email);

    User findByFirstName(String username);
}