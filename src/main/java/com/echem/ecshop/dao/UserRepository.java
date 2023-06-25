package com.echem.ecshop.dao;
import com.echem.ecshop.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
public interface UserRepository extends JpaRepository<User, Long> {
	User findFirstByName(String name);
}