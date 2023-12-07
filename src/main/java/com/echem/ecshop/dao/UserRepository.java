package com.echem.ecshop.dao;

import com.echem.ecshop.domain.User;
import org.springframework.data.domain.Example;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UserRepository extends JpaRepository<User, Long> {
    User findFirstByName(String name);
}