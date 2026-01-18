package com.echem.ecshop.dao;

import com.echem.ecshop.domain.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface OrderRepository extends JpaRepository<Order, Long> {
    
    @Query("SELECT o FROM Order o WHERE o.user.username = :username ORDER BY o.created DESC")
    List<Order> findByUsername(@Param("username") String username);
}
