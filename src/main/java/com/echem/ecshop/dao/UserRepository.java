package com.echem.ecshop.dao;

import com.echem.ecshop.domain.User;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findUserByEmail(String email);

    @Cacheable(cacheNames = "users", key = "#username")
    Optional<User> findUserByUsername(String username);

}