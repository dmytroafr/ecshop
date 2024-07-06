package com.echem.ecshop.dao;

import com.echem.ecshop.domain.ConfirmationToken;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface TokenRepository extends JpaRepository<ConfirmationToken,Long> {
    Optional<ConfirmationToken> findByToken(String token);
}
