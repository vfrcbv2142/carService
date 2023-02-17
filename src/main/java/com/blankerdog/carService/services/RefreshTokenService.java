package com.blankerdog.carService.services;


import com.blankerdog.carService.model.RefreshToken;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

public interface RefreshTokenService {
    Optional<RefreshToken> findByToken(String token);

    RefreshToken findById(long id);

    RefreshToken createRefreshToken(long accountId);

    RefreshToken verifyExpiration(RefreshToken token);

    void delete(long id);
}
