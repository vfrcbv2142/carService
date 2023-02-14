package com.blankerdog.carService.services;


import com.blankerdog.carService.model.RefreshToken;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

public interface RefreshTokenService {
    public Optional<RefreshToken> findByToken(String token);

    public RefreshToken createRefreshToken(Long accountId);

    public RefreshToken verifyExpiration(RefreshToken token);

    @Transactional
    public int deleteByAccountId(Long id);
}
