package com.echem.ecshop.dao;

import com.echem.ecshop.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findUserByEmail(String email);

    @Query("select u from User u where u.username= :username")
    Optional<User> findByUsername(@Param("username") String username);

}